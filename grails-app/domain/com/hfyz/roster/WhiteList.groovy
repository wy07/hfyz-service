package com.hfyz.roster
/**
 * 白名单信息
 */
class WhiteList {
    String vehicleNo //车牌号
    static constraints = {
        vehicleNo nullable: false, blank: false
    }
}
