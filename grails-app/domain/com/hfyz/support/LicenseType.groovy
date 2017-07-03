package com.hfyz.support

class LicenseType extends SystemCode{
    LicenseType parent

    static constraints = {
        parent nullable: true
        codeNum unique: 'class'
    }

    static mapping = {
        discriminator 'LICENSE_TYPE'
    }
}

