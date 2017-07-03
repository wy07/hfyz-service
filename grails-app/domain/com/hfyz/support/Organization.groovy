package com.hfyz.support

class Organization {
    String name
    String code
    Organization parent

    static constraints = {
        name nullable: false, blank: false, maxSize: 30,unique: 'parent'
        code nullable: false, blank: false, maxSize: 20,unique: true
        parent nullable: true
    }
}
