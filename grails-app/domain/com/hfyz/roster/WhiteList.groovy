package com.hfyz.roster
/**
 * 白名单信息
 */
class WhiteList {
    String vehicleNo //车牌号
    String frameNo // 车架号
    Date controlBegin // 解除布控时间
    Status status //布控状态
    static constraints = {
        vehicleNo nullable: false, blank: false
        frameNo nullable: false, blank: false
    }

    static mapping = {
        comment '白名单信息表'
        id generator:'native', params:[sequence:'white_list_seq'], defaultValue: "nextval('white_list_seq')"
        vehicleNo comment: '车牌号'
        frameNo comment: '车架号'
        controlBegin comment: '解除布控时间'
        status comment: '布控状态'
    }
}
