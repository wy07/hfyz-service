package com.hfyz.rectification


class HiddenRectificationOrder {

    String billNo               //单据编号
    String area                 //区域
    String enterprise           //业户名称
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
    Date lastUpdated            //更改时间
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

    static mapping = {
        comment '隐患整改单信息表'
        id generator:'native', params:[sequence:'hidden_rectification_order_seq'], defaultValue: "nextval('hidden_rectification_order_seq')"
        billNo comment: '单据编号'
        area comment: '区域'
        enterprise comment: '业户名称'
        companyCode comment: '业户编码'
        examiner comment: '检查人'
        inspectionDate comment: '检查日期'
        dealineDate comment: '整改期限'
        insPosition comment: '检查地点'
        insDesc comment: '检查内容'
        insQuestion comment: '存在问题'
        proPosal comment: '整改意见'
        replyDate comment: '反馈日期'
        replyDesc comment: '企业反馈'
        rectifiResult comment: '整改结果'
        lastUpdated comment: '最后更新时间'
        status comment: '状态'
    }

}
