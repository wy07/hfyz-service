package com.hfyz.owner

import com.commons.utils.SQLHelper
import grails.async.Promise
import grails.async.Promises
import grails.transaction.Transactional


/**
 * 经营业户_基本信息_业户标识
 */

@Transactional
class OwnerIdentityService {
    def dataSource

    def getOwnerList(def max, def offset, String ownerName, String companyCode, String dateBegin, String dateEnd) {
        def params = [:]
        def type = dateBegin && dateEnd
        if (ownerName) {
            params.ownerName = ownerName
        }
        if (companyCode) {
            params.companyCode = companyCode
        }
        if (type) {
            params.dateBegin = dateBegin
            params.dateEnd = dateEnd
        }

        Promise ownerCount = Promises.task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.firstRow(getCountSql(type, ownerName, companyCode), params).count ?: 0
            }

        }

        Promise ownerList = Promises.task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getListSql(type, ownerName, companyCode), params + [max: max, offset: offset])
            }?.collect { obj ->
                [id                 : obj.id,
                 ownerName          : obj.ownerName,
                 companyCode        : obj.companyCode,
                 ownerCode          : obj.ownerCode,
                 economicType       : obj.economicType,
                 legalRepresentative: obj.legalRepresentative,
                 operateManager     : obj.operateManager,
                 phone              : obj.phone,
                 endTime            : obj.endTime?.format("yyyy-MM-dd")]
            }
        }
        return [ownerList: ownerList.get(), total: ownerCount.get()]
    }

    /**
     * 列表查询sql
     * @param type 是否根据有效期查询，
     * @param ownerName 业户名称
     * @param companyCode 业户编码
     */
    private static getListSql(boolean type, String ownerName, String companyCode) {
        String listSql = """
          SELECT
            bas.id id,
            bas.owner_name ownerName,
            bas.company_code companyCode,
            bas.owner_code ownerCode,
            bas.economic_type economicType,
            bas.legal_representative legalRepresentative,
            bas.operate_manager operateManager,
            bas.phone phone,
            mag.end_time endTime
          FROM owner_basicinfo_owneridentity bas 
          LEFT JOIN owner_basicinfo_manageinfo mag ON bas.company_code = mag.company_code
          WHERE 1=1 """
        if (type) {
            listSql += " AND mag.end_time  BETWEEN to_timestamp(:dateBegin,'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:dateEnd,'YYYY-MM-DD HH24:MI:SS') "
        }
        if (ownerName) {
            listSql += " AND bas.owner_name=:ownerName "
        }
        if (companyCode) {
            listSql += " AND bas.company_code=:companyCode "
        }
        listSql += " limit :max offset :offset "
        return listSql
    }

    private static getCountSql(boolean type, String ownerName, String companyCode) {
        String countSql = "SELECT count(*) FROM owner_basicinfo_owneridentity bas  LEFT JOIN owner_basicinfo_manageinfo mag ON bas.company_code = mag.company_code WHERE 1=1 "
        if (type) {
            countSql += " AND mag.end_time  BETWEEN to_timestamp(:dateBegin,'YYYY-MM-DD HH24:MI:SS') and to_timestamp(:dateEnd,'YYYY-MM-DD HH24:MI:SS') "
        }
        if (ownerName) {
            countSql += " AND bas.owner_name=:ownerName "
        }
        if (companyCode) {
            countSql += " AND bas.company_code=:companyCode "
        }
        return countSql
    }
}
