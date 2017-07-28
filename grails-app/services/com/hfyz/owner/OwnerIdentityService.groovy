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
             , shortName                 : it.shortName     //业户简称
             , companyCode               : it.companyCode   //业户编码(组织机构代码）*
             , ownerCode                 : it.ownerCode     //企业单位代码
             , ownerAddress              : it.ownerAddress
             , postCode                  : it.postCode
             , administrativeDivisionName: it.administrativeDivisionName
             , administrativeDivisionCode: it.administrativeDivisionCode
             , economicType              : it.economicType
             , legalRepresentative       : it.legalRepresentative
             , idCardType                : it.idCardType
             , idCardNo                  : it.idCardNo
             , picture                   : it.picture        //法人代表照片
             , operateManager            : it.operateManager
             , phone                     : it.phone
             , fax                       : it.fax
             , telephone                 : it.telephone
             , email                     : it.email
             , website                   : it.website
             , parentCompanyName         : it.parentCompanyName   //企业单位代码
             , parentOwner               : it.parentOwner]    //母公司

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
