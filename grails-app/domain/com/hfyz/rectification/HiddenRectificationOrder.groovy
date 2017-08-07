package com.hfyz.rectification


class HiddenRectificationOrder {

    String billNo               //但据编号
    String area                 //区域
    String companyCode          //业户编码
    String examiner             //检查人
    Date   inspectionDate       //检查日期
    Date   dealineDate          //整改期限
    String insPosition          //检查地点
    String insDesc              //检查内容
    String insQuestion          //存在问题
    String proPosal             //整改意见
    Date   replyDate            //反馈日期
    String replyDesc            //企业反馈
    String rectifiResult        //整改结果
    HiddenRectificationOrderStatus status   //状态


    static constraints = {
        billNo unique: true, blank: false
        area nullable:true , blank:true
        companyCode nullable: false, blank: false
        examiner nullable: false, blank:false
        insPosition nullable: false, blank:false
        insDesc nullable: false, blank:false
        insQuestion nullable: false, blank:false
        proPosal  nullable: false, blank: true
        replyDate nullable: true
        replyDesc nullable: true, blank: true
        status nullable:false
        rectifiResult nullable: true
    }
}
