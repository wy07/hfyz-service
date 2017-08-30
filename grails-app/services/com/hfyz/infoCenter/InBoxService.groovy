package com.hfyz.infoCenter

import com.commons.utils.SQLHelper
import com.hfyz.security.User
import com.hfyz.workOrder.WorkOrder

class InBoxService {

    def dataSource
    def save(InfoCenter infoCenter) {
        boolean nextIsCompany = false
        def role = WorkOrder.get(infoCenter.sourceId).todoRole
        def sqlParams = [:]
        sqlParams.role = role
        sqlParams.id = infoCenter.id
        if(role == 'ROLE_COMPANY_ROOT'){
            nextIsCompany = true
            sqlParams.companyCode = WorkOrder.get(infoCenter.sourceId).companyCode
        }
        SQLHelper.withDataSource(dataSource) { sql ->
           sql.execute(saveInBoxSql(nextIsCompany), sqlParams)
        }

    }

    private static String saveInBoxSql(nextIsCompany) {
        String sqlStr = """     
               insert into in_box(version, date_created,info_center_id, accepter_id, source_id, source_type, title, is_read)
               select distinct 0, current_timestamp, info.id, user_id, info.source_id, info.source_type, info.title, false 
               from role, user_role, info_center info, work_order t_order, sys_user
               where role.authority = :role and user_role.role_id = role.id and info.id= :id
                """
               if(nextIsCompany){
                   sqlStr += " and sys_user.company_code = :companyCode and sys_user.id = user_role.user_id"
               }

        sqlStr +="; select currval(pg_get_serial_sequence('in_box','id'));"
        return sqlStr
    }
}
