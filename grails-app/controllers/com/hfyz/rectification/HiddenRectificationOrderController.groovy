package com.hfyz.rectification

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.commons.utils.NumberUtils
import com.hfyz.owner.OwnerIdentity
import grails.converters.JSON
import com.hfyz.owner.OwnerIdentityService

class HiddenRectificationOrderController implements ControllerHelper {

    def hiddenRectificationOrderService
    def ownerIdentityService

    def reviewApprovalList(){
        def hiddenRectificationOrder = HiddenRectificationOrder.get(params.long('id'))
        renderSuccessesWithMap(hiddenRectificationOrderService.getReviewAndApprovalList(hiddenRectificationOrder))
    }

    def submitOrder() {
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIns ->
                def tempStatus = hiddenRectificationOrderIns.status.id
                if(tempStatus == 0 || tempStatus == 3){
                    hiddenRectificationOrderIns.status = HiddenRectificationOrderStatus.DSH
                    hiddenRectificationOrderIns.save(flush: true,failOnError: true)
                }else{
                    renderErrorMsg("此单据已被提交")
                }

        }
        renderSuccess()
    }
    def list() {
        def user = getCurrentUser()
        def companyCode = user?user.companyCode : null
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(hiddenRectificationOrderService.getHiddenRectificationOrderList(request.JSON.company,
                request.JSON.startDate,request.JSON.endDate,max,offset, request.JSON.status,request.JSON.listStatus,companyCode))
    }

    def save(){
        HiddenRectificationOrder hiddenDanger = new HiddenRectificationOrder(request.JSON)
        hiddenDanger.inspectionDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.inspection)
        hiddenDanger.dealineDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.dealine)
        hiddenDanger.billNo = System.currentTimeMillis()+""+new Random().nextInt(100000).toString().padLeft(5, '0')
        hiddenDanger.status = HiddenRectificationOrderStatus.QC
        hiddenDanger.save(flush: true,failOnError: true)
        renderSuccess()

    }

    def getCompanyList(){
        renderSuccessesWithMap(ownerIdentityService.getCompanyListByChar(request.JSON.enterpirse))
    }

    def findCompanyNameByOwnerName(String name){
        return OwnerIdentity.findByOwnerName(name)
    }

    def findCompangCodeByOwnerCode(String code){
        return OwnerIdentity.findByCompanyCode(code)
    }

    def findReviewAndApprovalByBillId(def obj){
        return ReviewAndApprovalForm.findByBillId(obj)

    }

    def edit(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIn ->
                renderSuccessesWithMap([hiddenRectificationOrder:[
                        id : hiddenRectificationOrderIn.id,
                        area : hiddenRectificationOrderIn.area,
                        billNo : hiddenRectificationOrderIn.billNo,
                        enterpirse : findCompangCodeByOwnerCode(hiddenRectificationOrderIn.companyCode)?.ownerName,
                        examiner : hiddenRectificationOrderIn.examiner,
                        inspectionDate : hiddenRectificationOrderIn.inspectionDate.format('yyyy-MM-dd HH:mm:ss'),
                        dealineDate : hiddenRectificationOrderIn.dealineDate.format('yyyy-MM-dd HH:mm:ss'),
                        insPosition : hiddenRectificationOrderIn.insPosition,
                        insDesc : hiddenRectificationOrderIn.insDesc,
                        insQuestion : hiddenRectificationOrderIn.insQuestion,
                        proPosal : hiddenRectificationOrderIn.proPosal,
                        replyDate : hiddenRectificationOrderIn.replyDate?.format('yyyy-MM-dd HH:mm:ss'),
                        replyDesc : hiddenRectificationOrderIn.replyDesc,
                        status : hiddenRectificationOrderIn.status.type
                ]])
                        }

    }

    def delete(){
        withHiddenRectificationOrder(params.long('id')) {
            hiddenDanger ->
                hiddenDanger.delete(flush: true)
                renderSuccess()
        }
    }

    def update(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIns ->
                hiddenRectificationOrderIns.properties = request.JSON
                hiddenRectificationOrderIns.inspectionDate = request.JSON.inspection ? new Date()
                        .parse('yyyy-MM-dd HH:mm', request.JSON.inspection) : null
                hiddenRectificationOrderIns.dealineDate = request.JSON.dealine ? new Date()
                        .parse('yyyy-MM-dd HH:mm', request.JSON.dealine) : null
                hiddenRectificationOrderIns.status = HiddenRectificationOrderStatus.QC
                hiddenRectificationOrderIns.save(flush: true,failOnError: true)
                renderSuccess()
        }
    }

    def enterpriseFeedback(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderInstence ->
                def userCompanyCode = getCurrentUser().companyCode
                if(userCompanyCode == hiddenRectificationOrderInstence.companyCode){
                    def tempStatus = hiddenRectificationOrderInstence.status.id
                    if(tempStatus == 2){
                        hiddenRectificationOrderInstence.status =  HiddenRectificationOrderStatus.DYR
                    }
                    //hiddenRectificationOrderInstence.status = HiddenRectificationOrderStatus.getinstanceById(request.JSON.statusId)
                    hiddenRectificationOrderInstence.replyDate = request.JSON.reply ? new Date().parse('yyyy-MM-dd HH:mm', request.JSON.reply) : null
                    hiddenRectificationOrderInstence.replyDesc = request.JSON.replyDesc
                    hiddenRectificationOrderInstence.save(flush: true,failOnError: true)
                renderSuccess()
                }else{
                    renderErrorMsg("您没有此操作的权限！")
                }
        }
    }

    private withHiddenRectificationOrder(Long id,Closure c){
        HiddenRectificationOrder hiddenRectificationOrder = id ? HiddenRectificationOrder.get(id) : null
        if(hiddenRectificationOrder){
            c.call(hiddenRectificationOrder)
        }else{
            renderNoTFoundError()
        }
    }
}

