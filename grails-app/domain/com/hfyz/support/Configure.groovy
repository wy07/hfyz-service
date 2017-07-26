package com.hfyz.support

class Configure {

    String configKey
    def configValue
    String name

    static constraints = {
        configKey(nullable: false,blank: false,maxSize: 50)
        configValue(nullable: true,maxSize: 1500)
        name(nullable: false,blank: false,maxSize: 100)
    }

    String toString(){
        configKey+':'+configValue
    }
}
