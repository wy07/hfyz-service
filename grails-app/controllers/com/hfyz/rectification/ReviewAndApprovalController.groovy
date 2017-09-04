package com.hfyz.rectification

import com.commons.utils.ControllerHelper
import com.hfyz.owner.OwnerIdentity
import grails.converters.JSON

class ReviewAndApprovalController implements ControllerHelper {

    def infoCenterService

    def save(){
        ReviewAndApprovalForm reviewApproval =  new ReviewAndApprovalForm(request.JSON)
        reviewApproval.billId = HiddenRectificationOrder.get(request.JSON.billId)
        reviewApproval.approver = getCurrentUser().username
        reviewApproval.approveTime = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.time)
        reviewApproval.approveDesc = request.JSON.approveDesc
        if(Boolean.parseBoolean(request.JSON.tempStatus)){
            reviewApproval.approvalResult = true
        }else{
            reviewApproval.approvalResult = false
        }
        reviewApproval.save(flush:true,failOnError: true)
        HiddenRectificationOrder hiddenRectificationOrder = HiddenRectificationOrder.get(request.JSON.billId)
        def status= hiddenRectificationOrder.status
        if(status == HiddenRectificationOrderStatus.DSH){
            if(Boolean.parseBoolean(request.JSON.tempStatus)){
                hiddenRectificationOrder.status = HiddenRectificationOrderStatus.DFK
            }else{
                hiddenRectificationOrder.status = HiddenRectificationOrderStatus.YJJ
            }
        }
        hiddenRectificationOrder.save(flush:true,failOnError: true)
        infoCenterService.save(hiddenRectificationOrder.id, 'YHZGD')
        renderSuccess()
    }

    def giveResult(){
        HiddenRectificationOrder hiddenRectificationOrderins = HiddenRectificationOrder.get(params.long('id'))
        def status= hiddenRectificationOrderins.status
        if(status == HiddenRectificationOrderStatus.DYR) {
            if (request.JSON.tempStatus == 'true') {
                hiddenRectificationOrderins.status = HiddenRectificationOrderStatus.HG
                hiddenRectificationOrderins.rectifiResult = HiddenRectificationOrderStatus.HG.type
            } else {
                hiddenRectificationOrderins.status = HiddenRectificationOrderStatus.BHG
                hiddenRectificationOrderins.rectifiResult = HiddenRectificationOrderStatus.BHG.type
            }
        }
        hiddenRectificationOrderins.save(flush:true,failOnError: true)
        infoCenterService.save(hiddenRectificationOrderins.id, 'YHZGD')
        renderSuccess()
    }

}
