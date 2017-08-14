package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.support.DangerousType

class FreightWaybillController implements ControllerHelper {
    def freightWaybillService

    def search() {
        def userCompanyCode  = getCurrentUser().companyCode
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = freightWaybillService.search(request.JSON.vehicleNo, request.JSON.ownerName, request.JSON.dateBegin, request.JSON.dateEnd, max, offset,userCompanyCode)
        renderSuccessesWithMap(result)
    }
}
