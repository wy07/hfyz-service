package com.hfyz.support

/**
 * 货运站级别
 */
class FreightStationLevel extends SystemCode {
    FreightStationLevel parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'FREIGHT_STATION_LEVEL'
    }
}
