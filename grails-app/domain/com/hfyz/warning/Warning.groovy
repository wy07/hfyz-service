package com.hfyz.warning


class Warning {
    String frameNo             //车架号
    String carLicenseNo           //车牌号
    String carColor           //车辆颜色
    Integer warningSource          //报警信息来源
    String warningType    //报警类型
    Date warningTime   //报警时间
    Integer warningTimes   //报警次数
    Integer superviseId//报警督办ID
    Date endTime         //督办截止时间
    String superviseLevel         //督办级别
    String supervisePeople         //督办人
    String supervisePhone         //督办联系电话
    String superviseEmail         //督办联系电子邮箱


    static constraints = {
        frameNo unique: true, nullable: false, blank: false, maxSize: 17
        carLicenseNo unique: false, nullable: false, blank: false, maxSize: 8
        carColor unique: false, nullable: false, blank: false, maxSize: 10
        warningSource unique: false, nullable: false, blank: false
        warningType nullable: false, blank: false, maxSize: 50
        warningTime unique: false, nullable: false, blank: false
        warningTimes unique: false, nullable: false, blank: false, maxSize: 10
        superviseId unique: false, nullable: true, blank: true, maxSize: 4
        endTime unique: false, nullable: true, blank: true
        superviseLevel unique: false, nullable: true, blank: true,maxSize: 10
        supervisePeople unique: false, nullable: true, blank: true, maxSize: 16
        supervisePhone unique: false, nullable: true, blank: true, maxSize: 20
        superviseEmail unique: false, nullable: true, blank: true, maxSize: 30


    }
}
