package com.hfyz.roster
/**
 * 黑名单信息
 */
class BlackList {
    String vehicleNo //车牌号
    Date controlBegin //布控开始时间
    Date controlEnd //布控结束时间
    static constraints = {
        vehicleNo nullable: false, blank: false
        controlBegin nullable: false
        controlEnd nullable: false
    }
}
