package com.hfyz.owner

import com.commons.utils.SQLHelper
import com.hfyz.security.User
import grails.transaction.Transactional

/**
 * 企业内部管理制度
 */
@Transactional
class CompanyRegulationService {
    def dataSource

    /**
     * 列表搜索
     */
    def search(requestParams, max, offset, User user) {
        def begin = requestParams.dateBegin ? Date.parse("yyyy-MM-dd HH:mm:ss", requestParams.dateBegin) : ""
        def end = requestParams.dateEnd ? Date.parse("yyyy-MM-dd HH:mm:ss", requestParams.dateEnd) : ""
        def total = CompanyRegulation.createCriteria().get {
            projections {
                count()
            }
            if (user.isCompanyUser()) {
                eq("companyCode", user.companyCode)
            }
            if (requestParams.ownerName) {
                like("ownerName", "${requestParams.ownerName}%")
            }
            if (begin && end) {
                between("dateCreated", begin, end)
            }
        }

        def resultList = CompanyRegulation.createCriteria().list([max: max, offset: offset]) {
            if (user.isCompanyUser()) {
                eq("companyCode", user.companyCode)
            }
            if (requestParams.ownerName) {
                like("ownerName", "${requestParams.ownerName}%")
            }
            if (begin && end) {
                between("dateCreated", begin, end)
            }
        }?.collect({ CompanyRegulation regulation ->
            [
                    id            : regulation.id,
                    ownerName     : regulation.ownerName,
                    regulationName: regulation.regulationName,
                    fileName      : regulation.fileName,
                    fileType      : regulation.fileType,
                    fileSize      : regulation.fileSize,
                    dateCreated   : regulation.dateCreated?.format("yyyy-MM-dd HH:mm:ss"),
                    fileRealPath  : regulation.fileRealPath
            ]

        })

        return [resultList: resultList, total: total]
    }

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
