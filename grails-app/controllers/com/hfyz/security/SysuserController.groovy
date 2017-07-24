package com.hfyz.security

import com.commons.utils.NumberUtils
import com.commons.utils.SQLHelper
import grails.converters.JSON
import java.text.SimpleDateFormat
import com.commons.utils.ControllerHelper
import com.commons.utils.ValidationUtils

class SysuserController implements ControllerHelper {
    def dataSource
    def roleService
    def springSecurityService
    def UserService
    def static final DEFAULT_PASSWORD = '666666'

    def list() {
        renderSuccessesWithMap([userList: roleService.getUserList(NumberUtils.toInteger(request.JSON.operatorId))])
    }
   def save(){
       User user = new User(request.JSON)
       user.salt = ValidationUtils.secureRandomSalt
       user.passwordHash = DEFAULT_PASSWORD
       user.save(flush: true, failOnError: true)
       if(request.JSON.roles){
            Role.findAllByIdInList(request.JSON.roles).eachWithIndex{ role,index->
                UserRole.create user, role
                UserRole.withSession {
                    it.flush()
                    it.clear()
                }
            }
        }

        renderSuccess()
    }

    def edit() {
        println params
        withUser(params.long('id')) { user ->
            def result = []
            Role.list(sort: "id").each {
                result << [value: it.id, label: it.name]
            }
            renderSuccessesWithMap([user    : [name      : user.name
                                               , username: user.username
                                               , rights  : user.rights
                                               , id      : user.id
                                               , roles   : user.authorities.id
            ],
                                    roleList: result])
        }
    }

    def update() {
        withUser(params.long('id')) { userInstance ->
            String oldpwd = userInstance.password

            userInstance.properties = request.JSON
//            //userInstance.password = request.JSON.password
//            if(request.JSON.password!=oldpwd){
//                userInstance.salt = User.getSecureRandomSalt()
//                userInstance.password = encodePassword(request.JSON.password,userInstance.salt)
//            }

            userInstance.save(flush: true, failOnError: true)
            println request.JSON
            UserRole.removeAll userInstance
            Role.findAllByIdInList(request.JSON.roles).eachWithIndex { role, index ->

                UserRole.create userInstance, role
                UserRole.withSession {
                    it.flush()
                    it.clear()
                }
            }
        }
        renderSuccess()
    }

    def delete() {
        withUser(params.long('id')) { userInstance ->
            UserRole.removeAll(userInstance)
            userInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def resetPassword() {
        def userInstance = request.JSON.id ? User.findById(request.JSON.id) : null
        if (!userInstance) {
            renderNoTFoundError()
            return
        }

        def newPassword = UserService.resetPassword(userInstance)
        renderSuccessesWithMap([newPassword: newPassword])
    }

    def changePwd() {
        def currentUser = getCurrentUser()
        UserService.changePwd(currentUser, request.JSON.originPwd, request.JSON.newPwd)
        renderSuccessesWithMap([message: '密码修改成功!'])
    }

    private withUser(Long id, Closure c) {
        User userInstance = id ? User.get(id) : null

        if (userInstance) {
            c.call userInstance
        } else {
            renderNoTFoundError()
        }
    }


    def getUserByName() {
        println params
        def result = [:]
        def GET_USER_SQL = """
            select suser.id
                ,suser.date_created
                ,suser.last_updated
                ,suser.name
                ,suser.username
                ,suser.tel
                ,role.id role_id
                ,role.name role_name
            from sys_user suser, user_role ur,role
            where ur.user_id=suser.id
                and ur.role_id=role.id
                and  suser.username=:username
        """
        //println GET_USER_SQLde
        def userList = [:]
        def roleRights = []
        def roleList = [:]
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_USER_SQL.toString(), [username: request.JSON.name])
        }.eachWithIndex { user, index ->

            Role.findById(user.role_id).permissionGroups.each {
                if (it.menu != null) {
                    if (it.menu.parent) {
                        roleRights << "${it.menu.parent.code}:${it.menu.code}:${it.permissions}"
                    } else {
                        roleRights << "${it.menu.code}:${it.permissions}"
                    }
                } else {
                    roleRights << it.permissions
                }
            }
            if (!userList["${user.username}"]) {
                userList["${user.username}"] = [id           : user.id
                                                , name       : user.name
                                                , username   : user.username
                                                , dateCreated: user.date_created
                                                , lastUpdated: user.last_updated
                                                , tel        : user.tel
                                                //,rights:user.rights?.getArray()
                                                , roleId     : ''
                                                , roleName   : ''
                                                , roleRights : '']
                roleList['roleId'] = [user.role_id]
                roleList['roleName'] = [user.role_name]
            } else {
                roleList['roleId'] << user.role_id
                roleList['roleName'] << user.role_name
            }
        }
        if (userList?.size() > 0) {
            userList.values()[0].roleRights = roleRights.flatten().join(';')
            userList.values()[0].roleId = roleList['roleId'].join(',')
            userList.values()[0].roleName = roleList['roleName'].join(',')
            result = [user: userList.values()[0]]
        } else {
            result = [msg: "error!", errors: ['该用户不存在！']]
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        JSON.registerObjectMarshaller(java.sql.Timestamp) { o -> sdf.format(o) }
        println result
        render result as JSON
    }

    protected String encodePassword(password, salt) {
        return springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password, salt) : password
    }

    protected void notFound() {
        def map = ['result': 'error', 'errors': ['找不到该数据！']]
        render map as JSON
    }
    def renderError = { errorInstance ->
        def map = ['result': 'error', 'errors': errorInstance.errors.allErrors.collect {
            message(error: it, encodeAs: 'HTML')
        }]
        delegate.render map as JSON
    }

}
