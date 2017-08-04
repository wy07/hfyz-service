package com.hfyz.workOrder

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class WorkOrderController implements ControllerHelper{
    def workOrderService

    def list(){
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(workOrderService.findListAndTotal(max, offset))
    }
}
