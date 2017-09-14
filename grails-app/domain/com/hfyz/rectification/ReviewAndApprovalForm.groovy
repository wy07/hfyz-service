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

    static mapping = {
        comment '隐患整改单审批表'
        id generator:'native', params:[sequence:'review_approval_form_seq'], defaultValue: "nextval('review_approval_form_seq')"
        billId comment: '被审批的隐患整改单'
        approver comment: '审批人'
        approveTime comment: '审批时间'
        approveDesc comment: '审批意见'
        approvalResult comment: '审批结果'

    }
}
