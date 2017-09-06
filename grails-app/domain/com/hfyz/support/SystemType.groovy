package com.hfyz.support

class SystemType extends SystemCode {
    SystemType parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'SYSTEM_TYPE'
    }
}
