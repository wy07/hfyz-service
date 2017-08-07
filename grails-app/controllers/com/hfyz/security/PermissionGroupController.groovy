package com.hfyz.security

import com.commons.utils.PageUtils
import grails.converters.JSON
import com.commons.utils.ControllerHelper
import org.springframework.http.HttpMethod

class PermissionGroupController implements ControllerHelper{
    def roleService
    def springSecurityService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def permList = PermissionGroup.list([max: max, offset: offset, sort: 'id', order: 'desc']).collect { PermissionGroup perm ->
            perm as Map
        }
        def permCount = PermissionGroup.count()
        renderSuccessesWithMap([permList   : permList
                                , permCount: permCount])
    }
    def getPermission(){
        renderSuccessesWithMap([menuList:roleService.getPermission(request.JSON.menuid)])
    }
    def save(){
        PermissionGroup perm=new PermissionGroup(name:request.JSON.name
                ,category:request.JSON.category
                ,code:request.JSON.code
                ,url:request.JSON.url
                ,httpMethod:request.JSON.httpMethod ? HttpMethod.resolve(request.JSON.httpMethod) : null
                ,configAttribute:grailsApplication.config.getProperty("user.rootRole.name"))
        perm.save(failOnError: true)
        springSecurityService.clearCachedRequestmaps()
        renderSuccess()
    }

    def edit(){
        withPerm(params.long('id')) { PermissionGroup perm ->
            renderSuccessesWithMap([perm:perm as Map])
        }
    }

    def update(){
        withPerm(params.long('id')) { PermissionGroup perm ->
            perm.properties = request.JSON
            perm.httpMethod = request.JSON.httpMethod ? HttpMethod.resolve(request.JSON.httpMethod) : null
            perm.save(failOnError: true)
            springSecurityService.clearCachedRequestmaps()
            renderSuccess()
        }
    }

    def delete(){
        withPerm(params.long('id')) { PermissionGroup perm ->
            perm.delete()
            springSecurityService.clearCachedRequestmaps()
            renderSuccess()
        }
    }


    private withPerm(Long id, Closure c) {
        PermissionGroup permInstance = id ? PermissionGroup.get(id) : null

        if (permInstance) {
            c.call permInstance
        } else {
            renderNoTFoundError()
        }
    }
}
