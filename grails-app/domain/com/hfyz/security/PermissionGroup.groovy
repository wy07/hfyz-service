package com.hfyz.security

import com.hfyz.support.Menu
import org.springframework.http.HttpMethod

class PermissionGroup {

    String name
    String permissions
    Menu menu
//    String configAttribute
//    HttpMethod httpMethod
//    String url

    static constraints = {
        name nullable:false,unique:'menu',blank:false,maxSize:20
        permissions nullable:false,unique:'menu',blank:false,maxSize:20
        menu nullable:true,blank:false
//        configAttribute blank: false
//        httpMethod nullable: true
//        url blank: false, unique: 'httpMethod'
    }
}
