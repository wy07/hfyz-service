package com.hfyz.support

class SystemCode {

    String name
    String codeNum


    static constraints = {
        name nullable: false, blank: false, maxSize: 30
        codeNum nullable: false, blank: false, maxSize: 20
    }

    static mapping = {
        discriminator column: 'type', value: "SYSTEM_CODE"
    }

    String toString() {
        name
    }
}
