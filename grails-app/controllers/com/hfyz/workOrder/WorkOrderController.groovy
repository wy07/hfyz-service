package com.hfyz.workOrder

import com.commons.utils.ControllerHelper

class WorkOrderController implements ControllerHelper{
    def workOrderService

    def list(){
        def  result= workOrderService.list(request.JSON.max, request.JSON.offset)
        renderSuccessesWithMap([result: result])
    }
}
