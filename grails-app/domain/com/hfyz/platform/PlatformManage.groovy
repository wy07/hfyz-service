package com.hfyz.platform

class PlatformManage {
    String ip             //平台IP地址
    String port           //端口号
    String name           //平台名称
    String code           //平台代码
    String contactName    //平台联系人
    String contactPhone   //平台联系电话

    static constraints = {
        ip unique: true, blank: false, maxSize: 15
        port unique: true, blank: false, maxSize: 4
        name blank: false, maxSize: 30
        code unique: true, blank: false, maxSize: 20
        contactName blank: false, maxSize: 30
        contactPhone blank: false, maxSize: 11
    }

    static mapping = {
        comment '平台管理表'
        id generator:'native', params:[sequence:'platform_manage_seq'], defaultValue: "nextval('platform_manage_seq')"
        ip comment: '平台IP地址'
        port comment: '端口号'
        name comment: '平台名称'
        code comment: '平台代码'
        contactName comment: '平台联系人'
        contactPhone comment: '平台联系电话'

    }

}
