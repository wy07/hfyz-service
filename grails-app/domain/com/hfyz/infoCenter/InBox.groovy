package com.hfyz.infoCenter

import com.hfyz.security.User

class InBox {
    InfoCenter infoCenter
    User accepter
    Long sourceId
    SourceType sourceType
    String title
    Date dateCreated
    Boolean isRead = false

    static constraints = {
        infoCenter nullable: false
        accepter nullable: false
        sourceId nullable: false
        sourceType nullable: false
        title blank: false, nullable: false
    }

    static mapping = {
        id generator:'native', params:[sequence:'inBox_seq'], defaultValue: "nextval('inBox_seq')"
    }
}
