package com.hfyz.security

import com.commons.utils.NumberUtils
import com.commons.utils.PageUtils
import com.commons.utils.SQLHelper
import com.hfyz.owner.OwnerIdentity
import com.hfyz.support.Organization
import grails.converters.JSON
import java.text.SimpleDateFormat
import com.commons.utils.ControllerHelper
import com.commons.utils.ValidationUtils

class SysuserController implements ControllerHelper {
    def dataSource
    def roleService
    def springSecurityService
    def userService
    def ownerIdentityService
    static final String DEFAULT_PASSWORD = '666666'

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def userList = User.createCriteria().list([max:max, offset:offset, sort: 'id', order: 'desc']){
            if(getCurrentUser().isCompanyUser()){
                eq ("companyCode", getCurrentUser().companyCode)
            }
        }?.collect(){ User user ->
            [id           : user.id
             , name       : user.name
             , username   : user.username
             , dateCreated: user.dateCreated.format('yyyy-MM-dd HH:mm:ss ')
             , roles      : user.authorities?.name?.join('；') ?: ''
             , org        : user.org?.name
             , companyCode: user.companyCode]
        }
        def totalUsers = User.createCriteria().get {
            projections {
                count()
            }
            if(getCurrentUser().isCompanyUser()){
                eq ("companyCode", getCurrentUser().companyCode)
            }
        }
        renderSuccessesWithMap([userList: userList, totalUsers: totalUsers])
    }

    def save() {
        def companyCode
        def org
        if(!getCurrentUser().isAdmin()){
            companyCode = getCurrentUser().companyCode
            org = getCurrentUser().org
        }
        userService.save(request.JSON, companyCode, org)
        renderSuccess()
    }

    def edit() {
        withUser(params.long('id')) { User user ->
            def result = []
            Role.list(sort: "id").each {
                result << [value: it.id, label: it.name]
            }
            renderSuccessesWithMap([user    : [name         : user.name
                                               , username   : user.username
                                               , id         : user.id
                                               , roles      : user.authorities.id
                                               , companyCode: user.companyCode
                                               , orgId      : user.org?.id
                                               , orgCode    : user.org?.code
                                               , enterpirse : OwnerIdentity.findByCompanyCode(user.companyCode)?.ownerName
            ]])
        }

    }

    def update() {
        withUser(params.long('id')) { userInstance ->
            userService.update(userInstance, request.JSON)
            renderSuccess()
        }

    }

    def delete() {
        withUser(params.long('id')) { userInstance ->
            userService.delete(userInstance)
            renderSuccess()
        }
    }

    def getCompanyList(){
        renderSuccessesWithMap(ownerIdentityService.getCompanyListByChar(request.JSON.enterpirse))
    }

    def resetPassword() {
        def userInstance = request.JSON.id ? User.findById(request.JSON.id) : null
        if (!userInstance) {
            renderNoTFoundError()
            return
        }

        def newPassword = userService.resetPassword(userInstance)
        renderSuccessesWithMap([newPassword: newPassword])
    }

    def changePwd() {
        def currentUser = getCurrentUser()
        userService.changePwd(currentUser, request.JSON.originPwd, request.JSON.newPwd)
        renderSuccessesWithMap([message: '密码修改成功!'])
    }

    def home(){
        renderSuccessesWithMap(userService.getHomeStatistic(currentUser))
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

}
