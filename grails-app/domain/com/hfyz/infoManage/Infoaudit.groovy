package com.hfyz.infoManage

import com.hfyz.security.User


class Infoaudit {
    //int id
    String type          //发布类型
    User publisher      //发布者
    User receiver       //接收者
    User auditor        //审核者
    String title        //标题
    String content      //内容
    //String pathUrl
    Status status       //状态
    Date dateCreated   //创建时间
    Date vimTime       //最新编辑时间
    Date auditTime     //审核时间

    static constraints = {
        //id blank: false, unique: true,maxSize:20
        type nullable:false,blank: false,maxSize:11
        publisher nullable:false,blank: false
        receiver nullable:true,blank: true
        auditor nullable:true,blank: true
        title nullable:false,blank: false,maxSize:100
        content nullable:true , unique: false, maxSize:2000
        status nullable:false,blank: false
        vimTime nullable:true,blank: true
        auditTime nullable:true,blank: true, unique: true
    }


}
