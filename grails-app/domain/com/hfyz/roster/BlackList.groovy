package com.hfyz.roster
/**
 * 黑名单信息
 */
class BlackList {
    String vehicleNo //车牌号
    String frameNo // 车架号
    String blackType // 黑名单类型
    Date controlBegin //布控开始时间
    Date controlEnd //布控结束时间
    String controlBehavior //布控行为
    String scheme //报警方案
    String controlRange //布控范围
    String controlOrg //布控单位
    String executor //布控人
    Status status //布控状态
    static constraints = {
        vehicleNo nullable: false, blank: false
        controlBegin nullable: false
        controlEnd nullable: false
    }
}
