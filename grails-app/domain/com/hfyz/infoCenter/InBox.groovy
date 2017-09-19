package com.hfyz.infoCenter

import com.hfyz.security.User

class InBox {
    InfoCenter infoCenter
    User accepter
    Long sourceId
    SourceType sourceType
    String content
    Date dateCreated
    String action
    Boolean isRead = false
    static constraints = {
        infoCenter nullable: false
        accepter nullable: false
        sourceId nullable: false
        sourceType nullable: false
        content blank: false, nullable: false
        action blank: true, nullable: true
    }

    static mapping = {
        comment '消息表'
        id generator:'native', params:[sequence:'inbox_seq'], defaultValue: "nextval('inbox_seq')"
        infoCenter comment:'消息基础信息'
        accepter comment:'消息接收人'
        sourceId comment:'消息源'
        sourceType comment:'消息类型'
        content comment:'消息体'
        dateCreated comment:'创建时间'
        action comment:'操作动作'
        isRead comment:'是否已读'
    }
}
