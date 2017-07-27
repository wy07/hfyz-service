package com.hfyz.security

import grails.converters.JSON
import com.commons.utils.ControllerHelper
class PermissionGroupController implements ControllerHelper{
    def roleService
    def list() {
        renderSuccessesWithMap([menuList:roleService.getMenu(request.JSON.roles)])
    }
    def getPermission(){
        renderSuccessesWithMap([menuList:roleService.getPermission(request.JSON.menuid)])
    }
    def save(){
        def permission=request.JSON.permissions
        def role=Role.findById(request.JSON.id)
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
