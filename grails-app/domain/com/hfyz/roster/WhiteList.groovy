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
}
