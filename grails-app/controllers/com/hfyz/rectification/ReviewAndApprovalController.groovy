package com.hfyz.rectification

import com.commons.utils.ControllerHelper
import com.hfyz.owner.OwnerIdentity
import grails.converters.JSON

class ReviewAndApprovalController implements ControllerHelper {


    def save(){
        ReviewAndApprovalForm reviewApproval =  new ReviewAndApprovalForm(request.JSON)
        reviewApproval.billId = HiddenRectificationOrder.get(request.JSON.billId)
        reviewApproval.approver = getCurrentUser().username
        reviewApproval.approveTime = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.time)
        reviewApproval.approveDesc = request.JSON.approveDesc
        if(request.JSON.statusId == '2' ){
            reviewApproval.approvalResult = true
        }else{
            reviewApproval.approvalResult = false
        }
        reviewApproval.save(flush:true,failOnError: true)
        setStatus()
        renderSuccess()
    }

    def setStatus(){
        HiddenRectificationOrder hiddenRectificationOrder = HiddenRectificationOrder.get(request.JSON.billId)
        hiddenRectificationOrder.status = HiddenRectificationOrderStatus.getinstanceById(Integer.parseInt(request.JSON.statusId))
        hiddenRectificationOrder.save(flush:true,failOnError: true)
    }

    def giveResult(){
        HiddenRectificationOrder hiddenRectificationOrderins = HiddenRectificationOrder.get(params.long('id'))
        hiddenRectificationOrderins.rectifiResult = HiddenRectificationOrderStatus.getinstanceById(Integer.parseInt(request.JSON.statusId)).type.toString()
        hiddenRectificationOrderins.status = HiddenRectificationOrderStatus.getinstanceById(Integer.parseInt(request.JSON.statusId))
        hiddenRectificationOrderins.save(flush:true,failOnError: true)
        renderSuccess()
    }

}
