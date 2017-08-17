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
    def statistic(){

        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)

        def inputParams=[:]

        Date startDate=NumberUtils.getDate(request.JSON.startDate,'yyyy-MM-dd')
        if(!startDate){
            renderErrorMsg('请输入正确的起始时间')
            return
        }
        Date endDate=NumberUtils.getDate(request.JSON.endDate,'yyyy-MM-dd')
        if(!endDate){
            renderErrorMsg('请输入正确的终止时间')
            return
        }
        if(endDate<startDate){
            renderErrorMsg('终止时间不能小于起始时间')
            return
        }

        inputParams.startDate=startDate
        inputParams.endDate=endDate

        if(request.JSON.companyName){
            inputParams.companyName=request.JSON.companyName
        }
        if(request.JSON.alarmType){
            inputParams.alarmTypeId=NumberUtils.toLong(request.JSON.alarmType)
        }

        def result = workOrderService.statistic(inputParams, currentUser, max, offset)
        renderSuccessesWithMap([statisticList: result.statisticList, statisticCount: result.statisticCount])
    }

    def show(){
        withWorkOrder(params.long('id')){
            workOrderIns ->
                renderSuccessesWithMap(workOrder:workOrderIns as Map)
        }
    }

    private withWorkOrder(Long id,Closure c){
        WorkOrder workOrder = id ? WorkOrder.get(id) : null
        if(workOrder){
            c.call(workOrder)
        }else{
            renderNoTFoundError()
        }
    }
}
