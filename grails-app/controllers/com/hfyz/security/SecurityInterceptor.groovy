package com.hfyz.security

import grails.converters.JSON

class SecurityInterceptor {

    SecurityInterceptor() {
        matchAll()
    }

    boolean before() {
        println "${controllerName}/${actionName}"
        def gatewayName = request.getHeader('dgate-gateway')?.decodeBase64()
        if (gatewayName) {
            gatewayName = new String(gatewayName)
        }
        params.gatewayName = gatewayName

        def jwtToken = request.getHeader('dgate-jwt-token')?.decodeBase64()
        if (jwtToken) {
            jwtToken = JSON.parse(new String(jwtToken))
            jwtToken.id = jwtToken.id as Long
        }
        params.jwtToken = jwtToken

        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
