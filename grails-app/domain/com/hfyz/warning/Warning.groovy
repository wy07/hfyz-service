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
        frameNo blank: false, maxSize: 17
        carLicenseNo blank: false, maxSize: 8
        carColor blank: false, maxSize: 10
        warningSource blank: false, inList: [1, 2, 3, 9]
        warningType blank: false, maxSize: 50
        warningTime blank: false
        warningTimes nullable: false, blank: false, maxSize: 10
        superviseId nullable: true, maxSize: 4
        endTime nullable: true
        superviseLevel nullable: true, maxSize: 10, inList: [0, 1]
        supervisePeople nullable: true, maxSize: 16
        supervisePhone nullable: true, maxSize: 20
        superviseEmail nullable: true, maxSize: 30
    }
}
