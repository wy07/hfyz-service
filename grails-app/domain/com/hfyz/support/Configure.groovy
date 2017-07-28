package com.hfyz.support

class Configure {

    String configKey
    String configValue
    String name

    static constraints = {
        configKey nullable: false, blank: false, maxSize: 50
        configValue nullable: false, blank: false, maxSize: 1500
        name nullable: false,blank: false, maxSize: 100
    }

    String toString(){
        configKey+':'+configValue
    }
}
