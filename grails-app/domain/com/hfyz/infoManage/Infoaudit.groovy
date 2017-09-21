package com.hfyz.infoManage

import com.hfyz.security.User


class Infoaudit {
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
        type nullable: false, blank: false, maxSize: 11
        publisher nullable: false, blank: false
        receiver nullable: true, blank: true
        auditor nullable: true, blank: true
        title nullable: false, blank: false, maxSize: 100
        content nullable: true, unique: false, maxSize: 2000
        status nullable: false, blank: false
        vimTime nullable: true, blank: true
        auditTime nullable: true, blank: true, unique: true
    }

    static mapping = {
        comment '信息表'
        id generator: 'native', params: [sequence: 'infoaudit_seq'], defaultValue: "nextval('infoaudit_seq')"
        type comment: '发布类型'
        publisher comment: '发布者'
        receiver comment: '接收者'
        auditor comment: '审核者'
        title comment: '标题'
        content comment: '内容'
        status comment: '状态'
        dateCreated comment: '创建时间'
        vimTime comment: '最新编辑时间'
        auditTime comment: '审核时间'
    }

}
