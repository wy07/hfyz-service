package com.hfyz.owner

import com.commons.utils.ControllerHelper

/**
 * 经营业户_基本信息_业户标识
 */

class OwnerIdentityController implements ControllerHelper {

    def ownerIdentityService

    def list() {
        renderSuccessesWithMap([ownerList: ownerIdentityService.getOwnerList(request.JSON.max, request.JSON.offset, request.JSON.ownerName, request.JSON.companyCode)])
    }

    def view() {
        withOwner(params.long('id')) { owner ->
            renderSuccessesWithMap([owner: [id                          : owner.id
                                            , ownerName                 : owner.ownerName     //业户名称*
                                            , shortName                 : owner.shortName     //业户简称
                                            , companyCode               : owner.companyCode   //业户编码(组织机构代码）*
                                            , ownerCode                 : owner.ownerCode     //企业单位代码
                                            , ownerAddress              : owner.ownerAddress
                                            , postCode                  : owner.postCode
                                            , administrativeDivisionName: owner.administrativeDivisionName
                                            , administrativeDivisionCode: owner.administrativeDivisionCode
                                            , economicType              : owner.economicType
                                            , legalRepresentative       : owner.legalRepresentative
                                            , idCardType                : owner.idCardType
                                            , idCardNo                  : owner.idCardNo
                                            , picture                   : owner.picture        //法人代表照片
                                            , operateManager            : owner.operateManager
                                            , phone                     : owner.phone
                                            , fax                       : owner.fax
                                            , telephone                 : owner.telephone
                                            , email                     : owner.email
                                            , website                   : owner.website
                                            , parentCompanyName         : owner.parentCompanyName   //企业单位代码
                                            , parentOwner               : owner.parentOwner]])    //母公司

        }
    }

    private withOwner(Long id, Closure c) {
        OwnerIdentity ownerInstance = id ? OwnerIdentity.get(id) : null
        if (ownerInstance) {
            c.call ownerInstance
        } else {
            renderNoTFoundError()
        }

    }
}
