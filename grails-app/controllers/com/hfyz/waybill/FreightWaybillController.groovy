package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.support.DangerousType

class FreightWaybillController implements ControllerHelper {
    def freightWaybillService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = freightWaybillService.search([vehicleNo  : request.JSON.vehicleNo
                                                   , ownerName: request.JSON.ownerName
                                                   , dateBegin: request.JSON.dateBegin
                                                   , dateEnd  : request.JSON.dateEnd], currentUser, max, offset)
        renderSuccessesWithMap(result)
    }
}
