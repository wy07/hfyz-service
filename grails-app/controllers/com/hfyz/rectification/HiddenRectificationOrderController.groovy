package com.hfyz.rectification

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.infoCenter.SourceType
import com.hfyz.owner.OwnerIdentity
import grails.converters.JSON

class HiddenRectificationOrderController implements ControllerHelper {

    def hiddenRectificationOrderService
    def ownerIdentityService
    def infoCenterService
    def documentService
    def reviewApprovalList(){
        def hiddenRectificationOrder = HiddenRectificationOrder.get(params.long('id'))
        renderSuccessesWithMap(hiddenRectificationOrderService.getReviewAndApprovalList(hiddenRectificationOrder))
    }

    def submitOrder() {
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIns ->
                def tempStatus = hiddenRectificationOrderIns.status
                if(tempStatus == HiddenRectificationOrderStatus.QC || tempStatus == HiddenRectificationOrderStatus.YJJ){
                    hiddenRectificationOrderIns.status = HiddenRectificationOrderStatus.DSH
                    hiddenRectificationOrderIns.save(flush: true,failOnError: true)
                    infoCenterService.save(hiddenRectificationOrderIns.id, SourceType.YHZGD)
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
        def upload = request.getFile('upload')
        def json = JSON.parse(params.hiddenRectificationOrder)

        HiddenRectificationOrder hiddenDanger = new HiddenRectificationOrder(json)
        hiddenDanger.inspectionDate = new Date().parse('yyyy-MM-dd HH:mm', json.inspection)
        hiddenDanger.dealineDate = new Date().parse('yyyy-MM-dd HH:mm', json.dealine)
        hiddenDanger.billNo = System.currentTimeMillis()+""+new Random().nextInt(100000).toString().padLeft(5, '0')
        hiddenDanger.status = HiddenRectificationOrderStatus.QC
        hiddenDanger.enterprise = findOwnerIdentityByOrgCode(json.companyCode).name
        hiddenDanger.document = documentService.save(upload)
        hiddenDanger.save(flush: true,failOnError: true)
        renderSuccess()

    }

    def getCompanyList(){
        renderSuccessesWithMap(ownerIdentityService.getCompanyListByChar(request.JSON.enterpirse))
    }

    OwnerIdentity findOwnerIdentityByOrgCode(String code){
        return OwnerIdentity.findByOrgCode(code)
    }

    ReviewAndApprovalForm findReviewAndApprovalByBillId(def obj){
        return ReviewAndApprovalForm.findByBillId(obj)

    }

    def edit(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIn ->
                if(request.JSON.action){
                    if(request.JSON.action != hiddenRectificationOrderIn.status.name()){
                        renderErrorMsg('此整改单已被处理！')
                        return
                    }
                }

                def fileName = hiddenRectificationOrderIn.document?hiddenRectificationOrderIn.document.fileName+ '.' + hiddenRectificationOrderIn.document.fileType:null
                renderSuccessesWithMap([hiddenRectificationOrder:[
                        id : hiddenRectificationOrderIn.id,
                        area : hiddenRectificationOrderIn.area,
                        billNo : hiddenRectificationOrderIn.billNo,
                        enterpirse : hiddenRectificationOrderIn.enterprise,
                        companyCode : hiddenRectificationOrderIn.companyCode,
                        examiner : hiddenRectificationOrderIn.examiner,
                        inspectionDate : hiddenRectificationOrderIn.inspectionDate.format('yyyy-MM-dd HH:mm:ss'),
                        dealineDate : hiddenRectificationOrderIn.dealineDate.format('yyyy-MM-dd HH:mm:ss'),
                        insPosition : hiddenRectificationOrderIn.insPosition,
                        insDesc : hiddenRectificationOrderIn.insDesc,
                        insQuestion : hiddenRectificationOrderIn.insQuestion,
                        proPosal : hiddenRectificationOrderIn.proPosal,
                        replyDate : hiddenRectificationOrderIn.replyDate?.format('yyyy-MM-dd HH:mm:ss'),
                        replyDesc : hiddenRectificationOrderIn.replyDesc,
                        status : hiddenRectificationOrderIn.status.type,
                        fileName: fileName
                ]])
                        }
    }

    def delete(){
        withHiddenRectificationOrder(params.long('id')) {
            hiddenDanger ->
                def document = hiddenDanger.document
                hiddenDanger.document = null
                documentService.delete(document)
                hiddenDanger.delete(flush: true)
                renderSuccess()
        }
    }

    def update(){
        def upload = request.getFile('upload')
        def json = JSON.parse(params.hiddenRectificationOrder)
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIns ->
                hiddenRectificationOrderIns.properties = json
                hiddenRectificationOrderIns.inspectionDate = json.inspection ? new Date()
                        .parse('yyyy-MM-dd HH:mm', json.inspection) : null
                hiddenRectificationOrderIns.dealineDate = json.dealine ? new Date()
                        .parse('yyyy-MM-dd HH:mm', json.dealine) : null
                hiddenRectificationOrderIns.status = HiddenRectificationOrderStatus.QC
                hiddenRectificationOrderIns.enterprise = findOwnerIdentityByOrgCode(json.companyCode).name
                if(upload) {
                    def document = hiddenRectificationOrderIns.document
                    hiddenRectificationOrderIns.document = null
                    documentService.delete(document)
                    hiddenRectificationOrderIns.document = documentService.save(upload)
                }
                hiddenRectificationOrderIns.save(flush: true,failOnError: true)
                renderSuccess()
        }
    }

    def enterpriseFeedback(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderInstence ->
                def userCompanyCode = getCurrentUser().companyCode
                if(userCompanyCode == hiddenRectificationOrderInstence.companyCode){
                    def tempStatus = hiddenRectificationOrderInstence.status
                    if(tempStatus == HiddenRectificationOrderStatus.DFK){
                        hiddenRectificationOrderInstence.status =  HiddenRectificationOrderStatus.DYR
                    }

                    hiddenRectificationOrderInstence.replyDate = request.JSON.reply ? new Date().parse('yyyy-MM-dd HH:mm', request.JSON.reply) : null
                    hiddenRectificationOrderInstence.replyDesc = request.JSON.replyDesc
                    hiddenRectificationOrderInstence.save(flush: true,failOnError: true)
                    infoCenterService.save(hiddenRectificationOrderInstence.id, SourceType.YHZGD)
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

