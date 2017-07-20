package com.hfyz.support

class MapSignType extends SystemCode{

    MapSignType parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'MAP_SIGN_TYPE'
    }
}
