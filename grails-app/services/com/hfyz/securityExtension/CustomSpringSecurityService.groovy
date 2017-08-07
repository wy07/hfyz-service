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

    public findPremsByRole(String roleName){
        def conf = securityConfig
        return findRequestmapsByRole(roleName,conf)
    }

    public assignPerm(def role, List perms) {
        if (!role) {
            return
        }

        deleteAllPermOfRole(role)

        if (perms) {
            def permClass = getClassForName(securityConfig.requestMap.className)//getPermClass(role)
            String configAttributePropertyName = securityConfig.requestMap.configAttributeField
            String authorityFieldName = securityConfig.authority.nameField

            def perm
            perms.each { permId ->
                perm = permClass.get(permId)
                if (perm) {
                    String configAttribute = perm."$configAttributePropertyName"
                    List parts = configAttribute.split(',')*.trim()
                    parts.add(role."$authorityFieldName")
                    perm."$configAttributePropertyName" = parts.join(',')
                    perm.save()
                }
            }
        }
    }

    public deleteAllPermOfRole(def role) {
        if (!role) {
            return
        }

        String configAttributePropertyName = securityConfig.requestMap.configAttributeField
        String authorityFieldName = securityConfig.authority.nameField

        String roleName = role."$authorityFieldName"
        def perms = findPremsByRole(roleName)
        for (perm in perms) {
            String configAttribute = perm."$configAttributePropertyName"
            if (configAttribute == roleName) {
                perm.delete()
            } else {
                List parts = configAttribute.split(',')*.trim()
                parts.remove roleName
                perm."$configAttributePropertyName" = parts.join(',')
                perm.save()
            }
        }
        clearCachedRequestmaps()
    }
}
