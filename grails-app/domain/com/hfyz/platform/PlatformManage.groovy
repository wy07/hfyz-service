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


    static constraints = {
        ip nullable: false, unique:true,blank: false, maxSize: 15
        port nullable: false, unique:true,blank: false, maxSize: 4
        name nullable: true, blank: false, maxSize: 30
        code nullable: false, blank: false, maxSize: 20
        contactName nullable: false, blank: false, maxSize: 30
        contactPhone nullable: false, blank: false, maxSize: 11
        draftPeople nullable: false,blank: false,maxSize: 30
        status nullable: false,blank: false, maxSize: 4
    }

}
