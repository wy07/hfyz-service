package com.hfyz.owner

import com.commons.utils.ControllerHelper

/**
 * 经营业户_基本信息_业户标识
 */

class OwnerIdentityController  implements ControllerHelper {

    def ownerIdentityService

    def list() {
        renderSuccessesWithMap([platformList: ownerIdentityService.getOwnerList(request.JSON.max, request.JSON.offset, request.JSON.ownerName, request.JSON.companyCode)])
    }
}
