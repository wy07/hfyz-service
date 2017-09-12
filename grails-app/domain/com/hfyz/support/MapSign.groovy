package com.hfyz.support

class MapSign {

    String name
    MapSignType mapSignType
    BigDecimal longitude
    BigDecimal latitude
    boolean display = false

    static constraints = {
        name nullable: false, blank: false, maxSize: 20
        mapSignType nullable: false
        longitude nullable: false, min:-180.0000000, max:180.0000000, scale:7
        latitude nullable: false, min:-90.0000000, max:90.0000000, scale:7
    }

    static mapping = {
        comment '路标信息表'
        id generator:'native', params:[sequence:'map_sign_seq'], defaultValue: "nextval('map_sign_seq')"
        name comment: '路标名称'
        mapSignType comment: '路标类型'
        longitude comment: '经度'
        latitude comment: '纬度'
        display comment: '是否显示'
    }
}
