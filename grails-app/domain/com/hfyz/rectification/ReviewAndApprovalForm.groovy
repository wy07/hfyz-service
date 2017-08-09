package com.hfyz.rectification

class ReviewAndApprovalForm {

    HiddenRectificationOrder  billId        //隐患整改单id
    String  approver      //审批人
    Date    approveTime   //审批时间
    String  approveDesc   //审批意见
    Boolean approvalResult //审批结果

    static constraints = {
        approveDesc      nullable: false, blank:false
        approvalResult   nullable: true,blank: true
        approver         nullable: false
        billId           nullable: false
    }
}
