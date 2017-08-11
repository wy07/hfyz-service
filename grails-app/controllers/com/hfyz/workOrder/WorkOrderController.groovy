package com.hfyz.workOrder

import com.commons.utils.ControllerHelper
import com.commons.utils.NumberUtils
import com.commons.utils.PageUtils
import com.hfyz.security.User

class WorkOrderController implements ControllerHelper {
    def workOrderService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(workOrderService.findWorkOrderListAndTotal(max, offset,currentUser))
    }

    def pendingWorkOrderList() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(workOrderService.findPendingWorkOrderListAndTotal(max, offset, currentUser.authorities?.authority))
    }

    def feedbackWorkOrderList(){
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(workOrderService.findFeedbackWorkOrderListAndTotal(max, offset, currentUser))
    }

    def preExamine() {
        renderSuccessesWithMap(workOrderService.preExamine(params.long('id'), currentUser))
    }

    def examine() {
        if (!request.JSON.note) {
            renderParamsIllegalErrorMsg('审批意见不能为空')
            return
        }
        workOrderService.examine(params.long('id'), currentUser, request.JSON.note, NumberUtils.getBoolean(request.JSON.result))
        renderSuccess()
    }

    def preFeedback() {
        renderSuccessesWithMap(workOrderService.preFeedback(params.long('id'), currentUser))
    }

    def feedback(){

        if (!request.JSON.note) {
            renderParamsIllegalErrorMsg('反馈内容不能为空')
            return
        }
        workOrderService.feedback(params.long('id'), currentUser, request.JSON.note)
        renderSuccess()
    }

    def preJudge() {
        renderSuccessesWithMap(workOrderService.preJudge(params.long('id'), currentUser))
    }

    def judge() {
        if (!request.JSON.note) {
            renderParamsIllegalErrorMsg('研判意见不能为空')
            return
        }
        workOrderService.judge(params.long('id'), currentUser, request.JSON.note, NumberUtils.getBoolean(request.JSON.result))
        renderSuccess()
    }
}
