package com.hfyz.infoCenter

import com.commons.utils.SQLHelper
import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.rectification.HiddenRectificationOrderStatus
import com.hfyz.workOrder.WorkOrder
import com.hfyz.workOrder.WorkOrderStatus

class InBoxService {

    def dataSource
    def saveWorkOrder(InfoCenter infoCenter) {

        def flowList = WorkOrder.get(infoCenter.sourceId).flows
        def step = WorkOrder.get(infoCenter.sourceId).flowStep
        def action = step ? flowList[step-1].action : ''

        boolean nextIsCompany = false
        def role = WorkOrder.get(infoCenter.sourceId).todoRole
        def sqlParams = [:]
        sqlParams.role = role
        sqlParams.id = infoCenter.id
        sqlParams.action = action
        if(role == 'ROLE_COMPANY_ROOT'){
            nextIsCompany = true
            sqlParams.companyCode = WorkOrder.get(infoCenter.sourceId).companyCode
        }
        if(WorkOrder.get(infoCenter.sourceId).status == WorkOrderStatus.YWC) {
            nextIsCompany = true
            sqlParams.companyCode = WorkOrder.get(infoCenter.sourceId).companyCode
            sqlParams.action = WorkOrder.get(infoCenter.sourceId).status.name()
            sqlParams.role = 'ROLE_COMPANY_ROOT'
        }
        SQLHelper.withDataSource(dataSource) { sql ->
           sql.execute(saveWorkOrderInBoxSql(nextIsCompany), sqlParams)
        }

    }

    def saveHiddenRectificationOrder(InfoCenter infoCenter) {
        boolean nextIsCompany = false
        String menuCode = ''
        def status = HiddenRectificationOrder.get(infoCenter.sourceId).status
        if(status==HiddenRectificationOrderStatus.DFK || status==HiddenRectificationOrderStatus.HG
         || status==HiddenRectificationOrderStatus.BHG){
            nextIsCompany = true
            menuCode = 'enterpriseFeedback'
        }
        if(status==HiddenRectificationOrderStatus.DSH || status==HiddenRectificationOrderStatus.DYR){
            menuCode = 'orderExamine'
        }
        if(status==HiddenRectificationOrderStatus.YJJ){
            menuCode = 'hiddenDanger'
        }
        def sqlParams = [:]
        sqlParams.menuCode = menuCode
        sqlParams.id = infoCenter.id
        if(nextIsCompany){
            sqlParams.companyCode = HiddenRectificationOrder.get(infoCenter.sourceId).companyCode
        }
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.execute(saveHiddenRectificationInBoxSql(nextIsCompany), sqlParams)
        }

    }
    private static String saveHiddenRectificationInBoxSql(nextIsCompany) {
        String sqlStr = """     
                with t   
                as (select unnest(string_to_array(config_attribute, ',')) permission
                from menu 
                left join permission_group
                on menu.permission_code = permission_group.code 
                where menu.code = :menuCode)
                
                insert into in_box(version, date_created,info_center_id, accepter_id, source_id, source_type, title, is_read, action)
                select distinct 0, current_timestamp, info.id, user_id, info.source_id, info.source_type, info.title, false, hidden.status
                from role, user_role, info_center info, work_order t_order, sys_user, t, hidden_rectification_order hidden
                where role.authority = t.permission and user_role.role_id = role.id and info.id= :id and info.source_id = hidden.id
                """
                if(nextIsCompany){
                    sqlStr += " and sys_user.company_code = :companyCode and sys_user.id = user_role.user_id"
                }

        sqlStr +="; select currval(pg_get_serial_sequence('in_box','id'));"
        return sqlStr
    }


    private static String saveWorkOrderInBoxSql(nextIsCompany) {
        String sqlStr = """     
               insert into in_box(version, date_created,info_center_id, accepter_id, source_id, source_type, title, is_read, action)
               select distinct 0, current_timestamp, info.id, user_id, info.source_id, info.source_type, info.title, false, :action
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
