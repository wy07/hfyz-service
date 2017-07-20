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
}
