package com.hfyz.support

class LicenseType extends SystemCode{
    LicenseType parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'LICENSE_TYPE'
    }
}

