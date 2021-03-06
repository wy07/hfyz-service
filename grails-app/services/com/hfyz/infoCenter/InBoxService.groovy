package com.hfyz.infoCenter

import com.commons.utils.SQLHelper
import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.rectification.HiddenRectificationOrderStatus
import com.hfyz.waybill.FreightWaybill
import com.hfyz.workOrder.WorkOrder
import com.hfyz.workOrder.WorkOrderFlowAction
import com.hfyz.workOrder.WorkOrderStatus

class InBoxService {

    def dataSource
    def saveWorkOrderInBox(InfoCenter infoCenter) {

        boolean nextIsCompany = false
        def flowList = WorkOrder.get(infoCenter.sourceId).flows

        def role = WorkOrder.get(infoCenter.sourceId).todoRole
        def sqlParams = [:]
        sqlParams.role = role
        sqlParams.id = infoCenter.id
        if(WorkOrder.get(infoCenter.sourceId).status == WorkOrderStatus.DFK || WorkOrder.get(infoCenter.sourceId).status == WorkOrderStatus.YWC){
            nextIsCompany = true
            sqlParams.companyCode = WorkOrder.get(infoCenter.sourceId).companyCode
            flowList.each{flow ->
                if(flow.action == WorkOrderFlowAction.FK.name()) {
                    sqlParams.role = flow.role
                }
            }
        }
        SQLHelper.withDataSource(dataSource) { sql ->
           sql.execute(saveWorkOrderInBoxSql(nextIsCompany), sqlParams)
        }
    }

    def saveHiddenRectificationOrderOrFreightWaybillInBox(InfoCenter infoCenter) {
        boolean nextIsCompany = false
        def sqlParams = [:]
        sqlParams.id = infoCenter.id

        if(infoCenter.sourceType == SourceType.YHZGD) {
            def status = HiddenRectificationOrder.get(infoCenter.sourceId).status
            sqlParams.action = status.id
            sqlParams.menuCode = getMenuCode(status, infoCenter.sourceType)
            if(getMenuCode(status, infoCenter.sourceType) == 'enterpriseFeedback') {
                nextIsCompany = true
            }
            if(nextIsCompany){
                sqlParams.companyCode = HiddenRectificationOrder.get(infoCenter.sourceId).companyCode
            }
        }else if(infoCenter.sourceType == SourceType.DZLD) {
            def status = FreightWaybill.get(infoCenter.sourceId).status
            sqlParams.action = status
            sqlParams.menuCode = getMenuCode(status, infoCenter.sourceType)
            if(getMenuCode(status, infoCenter.sourceType) == 'freightWaybill') {
                nextIsCompany = true
            }
            if(nextIsCompany){
                sqlParams.companyCode = FreightWaybill.get(infoCenter.sourceId).companyCode
            }
        }

        SQLHelper.withDataSource(dataSource) { sql ->
            sql.execute(saveHiddenRectificationOrFreightWaybillInBoxSql(nextIsCompany), sqlParams)
        }

    }

    private getMenuCode(status, sourceType){
        String menuCode = ''
        if(sourceType == SourceType.YHZGD) {
            if (status == HiddenRectificationOrderStatus.DFK || status == HiddenRectificationOrderStatus.HG
                    || status == HiddenRectificationOrderStatus.BHG) {
                menuCode = 'enterpriseFeedback'
            } else if (status == HiddenRectificationOrderStatus.DSH || status == HiddenRectificationOrderStatus.DYR) {
                menuCode = 'orderExamine'
            } else if (status == HiddenRectificationOrderStatus.YJJ) {
                menuCode = 'hiddenDanger'
            }
        }else if(sourceType == SourceType.DZLD) {
            if(status == 'SHZ') {
                menuCode = 'freightWaybillApprove'
            }else if(status == 'YJJ' || status == 'YJS') {
                menuCode = 'freightWaybill'
            }
        }
        return menuCode
    }

    private static String saveHiddenRectificationOrFreightWaybillInBoxSql(nextIsCompany) {
        String sqlStr = """     
                with t   
                as (select unnest(string_to_array(config_attribute, ',')) permission
                from menu 
                left join permission_group
                on menu.permission_code = permission_group.code 
                where menu.code = :menuCode)
                
                insert into in_box(version, date_created,info_center_id, accepter_id, source_id, source_type, content, is_read, action)
                select distinct 0, current_timestamp, info.id, user_id, info.source_id, info.source_type, info.content, false, :action
                from role, user_role, info_center info, sys_user, t
                where role.authority = t.permission and user_role.role_id = role.id and info.id= :id
                """
                if(nextIsCompany){
                    sqlStr += " and sys_user.company_code = :companyCode and sys_user.id = user_role.user_id"
                }

        sqlStr +="; select currval(pg_get_serial_sequence('in_box','id'));"
        return sqlStr
    }


    private static String saveWorkOrderInBoxSql(nextIsCompany) {
        String sqlStr = """     
               insert into in_box(version, date_created,info_center_id, accepter_id, source_id, source_type, content, is_read, action)
               select distinct 0, current_timestamp, info.id, user_id, info.source_id, info.source_type, info.content, false, t_order.status
               from role, user_role, info_center info, work_order t_order, sys_user
               where role.authority = :role and user_role.role_id = role.id and info.id= :id and t_order.id = info.source_id
                """
               if(nextIsCompany){
                   sqlStr += " and sys_user.company_code = :companyCode and sys_user.id = user_role.user_id"
               }

        sqlStr +="; select currval(pg_get_serial_sequence('in_box','id'));"
        return sqlStr
    }
}
