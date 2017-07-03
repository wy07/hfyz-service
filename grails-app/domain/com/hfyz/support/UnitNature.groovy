package com.hfyz.support

class UnitNature extends SystemCode{
    UnitNature parent

    static constraints = {
        parent nullable: true
        codeNum unique: 'class'
    }

    static mapping = {
        discriminator 'UNIT_NATURE'
    }
}
