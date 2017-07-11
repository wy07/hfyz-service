package com.hfyz.security

class SecurityInterceptor {
    SecurityInterceptor() {
        matchAll()
    }

    boolean before() {
        println ""
        println "------------SecurityInterceptor-in"
        println "${controllerName}/${actionName}"
//        println request.JSON
        println "------------SecurityInterceptor-end"
        return true
    }
}
