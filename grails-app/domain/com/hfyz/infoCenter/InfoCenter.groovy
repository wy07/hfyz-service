package com.hfyz.infoCenter

class InfoCenter {

    Long sourceId
    SourceType sourceType
    String content
    Date dateCreated
    static constraints = {
        sourceId nullable: false
        sourceType nullable: false
        content blank: false, nullable: false
    }

    static mapping = {
        comment '消息基础表'
        id generator:'native', params:[sequence:'infocenter_seq'], defaultValue: "nextval('infocenter_seq')"
        sourceId comment:'消息源'
        sourceType comment:'消息类型'
        content comment:'消息体'
        dateCreated comment:'创建时间'
    }
}
