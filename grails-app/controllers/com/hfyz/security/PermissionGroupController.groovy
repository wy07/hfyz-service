package com.hfyz.security

import grails.converters.JSON
import com.commons.utils.ControllerHelper
class PermissionGroupController implements ControllerHelper{
    def roleService
    def list() {
        renderSuccessesWithMap([menuList:roleService.getMenu(params.roles)])
    }
    def getPermission(){
        renderSuccessesWithMap([menuList:roleService.getPermission(params.menuid)])
    }
    def save(){
        println params
        def permission=request.JSON
        println permission
        def role=Role.findById(params.id)
        println PermissionGroup.findAllByIdInList(permission)
        if(role){
            role.permissionGroups=PermissionGroup.findAllByIdInList(permission)
            role.save(flush: true, failOnError: true)
            renderSuccess()
        }else{
            notFound()
        }
    }
    protected void notFound() {
        def map=['result':'error','errors':['找不到该数据！']]  
        render map as JSON
    }
    def renderError= { errorInstance ->
        def map=['result':'error','errors':errorInstance.errors.allErrors.collect {
            message(error:it,encodeAs:'HTML')
        }]
        delegate.render map as JSON
    }
}
