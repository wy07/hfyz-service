package com.hfyz.owner

import com.commons.utils.SQLHelper
import grails.transaction.Transactional

/**
 * 企业内部管理制度
 */
@Transactional
class CompanyRegulationService {
    def dataSource

//    企业制度文件巡检
    def patrolCompanyRegulation() {
        return SQLHelper.withDataSource(dataSource) { sql -> sql.rows(PATROL_COMPANY_REGULATION) }
    }

    private static final PATROL_COMPANY_REGULATION = """
      select a.owner_name  ownerName 
        ,a.company_code companyCode
        ,a.operate_manager operateManager
        ,a.phone
        ,b.company_code
        from owner_identity a LEFT JOIN company_regulation b on a.company_code=b.company_code
        WHERE b.company_code is NULL
    """

}
