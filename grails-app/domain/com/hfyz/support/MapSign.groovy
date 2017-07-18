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
        longitude nullable: false, min:-180, max:180, scale:7
        latitude nullable: true, min:-90, max:90, scale:7
    }
}
