package com.hfyz.support

class Configure {

    String configKey
    String configValue
    String name
    String note

    static constraints = {
        configKey nullable: false, blank: false, unique: true, maxSize: 50
        configValue nullable: false, blank: false, maxSize: 1500
        name nullable: false, blank: false, unique: true, maxSize: 100
        note nullable: true, blank: true, maxSize: 1500
    }

    String toString(){
        configKey+':'+configValue
    }
}
