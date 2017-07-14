package com.hfyz.support

class UnitNature extends SystemCode{
    UnitNature parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'UNIT_NATURE'
    }
}
