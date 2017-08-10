package com.hfyz.security

import com.commons.utils.NumberUtils
import com.commons.utils.PageUtils
import com.commons.utils.SQLHelper
import com.hfyz.support.Organization
import grails.converters.JSON
import java.text.SimpleDateFormat
import com.commons.utils.ControllerHelper

class RoleController implements ControllerHelper {
    def dataSource
    def roleService
    def supportService
    def springSecurityService
    def userService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def roleList = Role.list([max: max, offset: offset, sort: 'id', order: 'desc']).collect { Role role ->
            [id         : role.id
             , name     : role.name
             , authority: role.authority
             , org      : role.org?.name]
        }
        def roleCount = Role.count()
        renderSuccessesWithMap([roleList   : roleList
                                , roleCount: roleCount])
    }

    def listForSelect() {
        User user = currentUser
        def roleList = []
        def orgList = []
        if (userService.isSuperAdmin(user.id)) {
            roleList = Role.list([sort: 'id', order: 'desc'])
            orgList = supportService.getOrgs()?.collect { Organization org ->
                [value: org.id, label: org.name]
            }
        } else {
            roleList = Role.findAllByOrg(user.org)
            orgList = user.org ? [[value: user.org.id, label: user.org.name]] : []
        }
        renderSuccessesWithMap([
                roleList : roleList?.collect { Role role ->
                    [value: role.id, label: role.name]
                }
                , orgList: orgList])
    }

    def save() {
        Role role = new Role(request.JSON)
        role.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withRole(params.long('id')) { Role role ->
            renderSuccessesWithMap([role   : [id         : role.id
                                              , name     : role.name
                                              , authority: role.authority
                                              , orgId    : role.org?.id],
                                    orgList: supportService.getOrgs()?.collect { Organization org ->
                                        [value: org.id, label: org.name]
                                    }])
        }
    }

    def update() {
        withRole(params.long('id')) { roleInstance ->
            if (!springSecurityService.updateRole(roleInstance, request.JSON)) {
                renderErrorMsg('系统忙，请稍后再试')
                return
            }
            renderSuccess()
        }

    }

    def delete() {
        withRole(params.long('id')) { roleInstance ->
            springSecurityService.deleteRole(roleInstance)
            renderSuccess()
        }
    }

    def preAssignPerm() {
        def roleInstance = params.id ? Role.get(params.id) : null
        def rolePermIdList = roleService.getRolePermIdList(roleInstance)
        def allPerm = [:]
        roleService.getAllPrem().each { PermissionGroup perm ->
            if (!allPerm["${perm.category}"]) {
                allPerm["${perm.category}"] = [label          : perm.category
                                               , data         : ''
                                               , expanded     : true
                                               , children     : []
                                               , selectedPerms: []]
            }
            allPerm["${perm.category}"].children << [label : perm.name
                                                     , data: [id        : perm.id
                                                              , selected: perm.id in rolePermIdList]]
        }
        renderSuccessesWithMap([perms: allPerm.values()])
    }

    def assignPerm() {
        withRole(params.long('id')) { roleInstance ->
            if(roleInstance.authority==grailsApplication.config.getProperty("user.rootRole.name")){
                renderErrorMsg('不能对超级管理员进行权限分配')
                return
            }

            roleService.assignPerm(roleInstance, request.JSON.perms)
            renderSuccess()
        }
    }

    private withRole(Long id, Closure c) {
        Role roleInstance = id ? Role.get(id) : null

        if (roleInstance) {
            c.call roleInstance
        } else {
            renderNoTFoundError()
        }
    }

    def getUsersByRoleId() {
        def GET_USERS_SQL = "select * from sys_user sysuser where exists (select user_id from user_role ur where ur.user_id=sysuser.id and ur.role_id=:roleId)"
        def result = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_USERS_SQL.toString(), [roleId: NumberUtils.toInteger('roleId')])
        }
        render result as JSON
    }
}
