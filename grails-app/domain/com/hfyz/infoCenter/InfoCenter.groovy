package com.hfyz.infoCenter

class InfoCenter {

    Long sourceId
    SourceType sourceType
    String title
    Date dateCreated
    static constraints = {
        sourceId nullable: false
        sourceType nullable: false
        title blank: false, nullable: false
    }

    static mapping = {
        id generator:'native', params:[sequence:'infoCenter_seq'], defaultValue: "nextval('infoCenter_seq')"
    }
}
