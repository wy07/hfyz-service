package com.hfyz.support

class Configure {

    String configKey
    String configValue
    String name
    String note

    static constraints = {
        configKey nullable: false, blank: false, unique: true, maxSize: 50
        configValue nullable: false, blank: false, maxSize: 1500
        name nullable: false, blank: false, unique: true, maxSize: 100
        note nullable: true, blank: true, maxSize: 1500
    }

    static mapping = {
        comment '系统配置表'
        id generator:'native', params:[sequence:'configure_seq'], defaultValue: "nextval('configure_seq')"
        configKey comment: '系统配置键'
        configValue comment: '系统配置值'
        name comment: '系统配置名称'
        note comment: '备注'
    }

    String toString(){
        configKey+':'+configValue
    }
}
