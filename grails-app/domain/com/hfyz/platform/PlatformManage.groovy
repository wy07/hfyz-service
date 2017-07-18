package com.hfyz.platform

class PlatformManage {
    String ip             //平台IP地址
    String port           //端口号
    String name           //平台名称
    String code           //平台代码
    String contactName    //平台联系人
    String contactPhone   //平台联系电话
    String draftPeople    //起草人
    String status       //状态
    Integer carNum       //平台车辆数
    Integer onLineNum       //在线车辆数
    Integer allOnLineNum       //历史上线车辆数
    Integer illegalNum       //违法违规车辆数
    Integer outLineNum       //平台未上线车辆
//    Integer platformOnLineNum       //接入平台在线车辆
//    Integer platformAllOnLineNum       //接入平台历史上线车辆


    static constraints = {
        ip unique: true,nullable: false,  blank: false, maxSize: 15
        port  unique: true, nullable: false,blank: false, maxSize: 4
        name  unique: false, nullable: false, blank: false, maxSize: 30
        code  unique: true, nullable: false, blank: false, maxSize: 20
        contactName  unique: false, nullable: false, blank: false, maxSize: 30
        contactPhone  unique: false, nullable: false, blank: false, maxSize: 11
        draftPeople unique: false,  nullable: true, blank: true, maxSize: 30
        status  unique: false, nullable: true, blank: true, maxSize: 4
        carNum  unique: false, nullable: true, blank: false, maxSize: 16
        onLineNum  unique: false, nullable: true, blank: false, maxSize: 16
        allOnLineNum  unique: false, nullable: true, blank: true, maxSize: 16
        illegalNum  unique: false, nullable: true, blank: true, maxSize: 16
        outLineNum  unique: false, nullable: true, blank: true, maxSize: 16

    }

}
