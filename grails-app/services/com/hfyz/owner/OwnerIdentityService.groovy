package com.hfyz.owner

import grails.transaction.Transactional

/**
 * 经营业户_基本信息_业户标识
 */

@Transactional
class OwnerIdentityService {

    def getOwnerList(def max, def offset, String ownerName, String companyCode) {

//        企业名称、组织机构代码 查询时间
        def ownerList = OwnerIdentity.createCriteria().list([max: max, offset: offset]) {
            if (ownerName) {
                like("ownerName", "%${ownerName}%")
            }
            if (companyCode) {
                like("companyCode", "%${companyCode}%")
            }

        }?.collect { OwnerIdentity it ->
            [id                          : it.id
             , ownerName                 : it.ownerName     //业户名称*
             , companyCode               : it.companyCode   //业户编码(组织机构代码）*
             , ownerCode                 : it.ownerCode     //企业单位代码
             , economicType              : it.economicType
             , legalRepresentative       : it.legalRepresentative
             , operateManager            : it.operateManager
             , phone                     : it.phone]
        }

        def total = OwnerIdentity.createCriteria().get {
            projections {
                count()
            }
            if (ownerName) {
                like("ownerName", "%${ownerName}%")
            }
            if (companyCode) {
                like("companyCode", "%${companyCode}%")
            }
        }
        return [ownerList: ownerList, total: total]


    }
}
