package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class PassLineBusinessBasicInfoController implements ControllerHelper {
    def passLineBusinessBasicInfoService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = passLineBusinessBasicInfoService.search(request.JSON.ownerName, max, offset)
        renderSuccessesWithMap(result)
    }
}
