package com.hfyz.owner

import com.commons.utils.SQLHelper
import com.hfyz.security.User
import grails.async.Promise
import grails.async.Promises
import grails.transaction.Transactional


/**
 * 经营业户_基本信息_业户标识
 */

@Transactional
class OwnerIdentityService {
    def dataSource

    def getOwnerList(
            def max,
            def offset, String ownerName, String orgCode, String dateBegin, String dateEnd, String userCompanyCode) {
        def params = [:]
        def type = dateBegin && dateEnd
        if (ownerName) {
            params.name = ownerName
        }
        if (orgCode) {
            params.orgCode = orgCode
        }
        if (type) {
            params.dateBegin = dateBegin
            params.dateEnd = dateEnd
        }
        if (userCompanyCode) {
            params.userCompanyCode = userCompanyCode
        }
        Promise ownerCount = Promises.task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.firstRow(getCountSql(type, ownerName, orgCode, userCompanyCode), params).count ?: 0
            }

        }

        Promise ownerList = Promises.task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getListSql(type, ownerName, orgCode, userCompanyCode), params + [max: max, offset: offset])
            }?.collect { obj ->
                [id                 : obj.id,
                 ownerName          : obj.name,
                 companyCode        : obj.orgCode,
                 ownerCode          : obj.orgCode,
                 economicType       : obj.economicType,
                 legalRepresentative: obj.legalRepresentative,
                 operateManager     : obj.operateManager,
                 phone              : obj.phone,
                 endTime            : obj.endTime?.format("yyyy-MM-dd")]
            }
        }
        return [ownerList: ownerList.get(), total: ownerCount.get()]
    }

    def getAll(User user) {
        def resultList = OwnerIdentity.createCriteria().list() {
            if (user.isCompanyUser()) {
                eq('orgCode', user.companyCode)
            }
        }?.collect({ OwnerIdentity info ->
            [
                    code: info.orgCode,
                    name: info.name
            ]
        })
        return resultList
    }

    /**
     * 列表查询sql
     * @param type 是否根据有效期查询，
     * @param ownerName 业户名称
     * @param companyCode 业户编码
     */
    private static getListSql(boolean type, String ownerName, String orgCode, String userCompanyCode) {
        String listSql = """
          SELECT
            bas.id id,
            bas.name name,
            bas.org_code orgCode,
            bas.economic_type economicType,
            bas.legal_representative legalRepresentative,
            bas.operate_manager operateManager,
            bas.phone phone,
            mag.end_time endTime
          FROM owner_basicinfo_owneridentity bas
          LEFT JOIN owner_basicinfo_manageinfo mag ON bas.org_code = mag.org_code
          WHERE 1=1 """
        if (type) {
            listSql += " AND mag.end_time  BETWEEN to_timestamp(:dateBegin,'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:dateEnd,'YYYY-MM-DD HH24:MI:SS') "
        }
        if (ownerName) {
            listSql += " AND bas.name=:name "
        }
        if (orgCode) {
            listSql += " AND bas.org_code=:orgCode "
        }
        if (userCompanyCode) {
            listSql += " and bas.org_code=:userCompanyCode"
        }
        listSql += " limit :max offset :offset "
        return listSql
    }

    private static getCountSql(boolean type, String ownerName, String orgCode, String userCompanyCode) {
        String countSql = "SELECT count(*) FROM owner_basicinfo_owneridentity bas  LEFT JOIN owner_basicinfo_manageinfo mag ON bas.org_code = mag.org_code WHERE 1=1 "
        if (type) {
            countSql += " AND mag.end_time  BETWEEN to_timestamp(:dateBegin,'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:dateEnd,'YYYY-MM-DD HH24:MI:SS') "
        }
        if (ownerName) {
            countSql += " AND bas.name=:name "
        }
        if (orgCode) {
            countSql += " AND bas.org_code=:orgCode "
        }
        if (userCompanyCode) {
            countSql += " AND bas.org_code=:userCompanyCode "
        }
        return countSql
    }

    def getCompanyListByChar(String companyName) {
        def companyList = OwnerIdentity.createCriteria().list() {
            if (companyName) {
                like("name", "${companyName}%")
            }
        }?.collect {
            OwnerIdentity obj ->
                [ownerName  : obj.name,
                 companyCode: obj.orgCode]
        }
        return [companyList: companyList]
    }

    def getAppraiseStatistic(String ownerName) {
        def result = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(getAppraiseStatisticSql(ownerName))
        }?.collect { obj ->
            [ownerName  : obj.ownername,
             companyCode: obj.orgCode,
             total      : obj.total]
        }
        return result
    }

    private static getAppraiseStatisticSql(String ownerName) {
        String sql = "select ownid.name ownerName,ownid.org_code orgCode, count(cr.company_code) total from owner_basicinfo_owneridentity ownid " +
                " left join company_regulation cr on cr.company_code = ownid.company_code "
        if (ownerName) {
            sql += " where ownid.name like '${ownerName}%' "
        }
        sql += " group by ownid.org_code, ownid.name " +
                " order by ownid.name asc;"
        return sql
    }
}
