package com.hfyz.securityExtension

import grails.plugin.springsecurity.SpringSecurityService
import grails.transaction.Transactional

@Transactional
class CustomSpringSecurityService extends SpringSecurityService{

    def serviceMethod() {

    }

    public Set findRequestmapsByRoles(List roleNames){
        def conf = securityConfig
        Set requestmaps=[]
        roleNames.each {String roleName->
            requestmaps+=findRequestmapsByRole(roleName,conf)
        }
        return requestmaps
    }
}
