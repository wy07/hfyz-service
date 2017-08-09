package com.hfyz.security

import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
import com.commons.utils.NumberUtils
import com.commons.utils.ValidationUtils

import grails.transaction.Transactional

@Transactional
class UserService {

    def springSecurityService
    def grailsApplication

    static final String DEFAULT_PASSWORD = '666666'


    def changePwd(User user, String originPwd, String newPwd) {
        if (!user) {
            throw new RecordNotFoundException()
        }
        String originPasswordHash = springSecurityService.encodePassword(originPwd, user.salt)
        if (originPasswordHash != user.passwordHash) {
            throw new ParamsIllegalException('旧密码错误')
        }
        if (!ValidationUtils.isStrongPassword(newPwd)) {
            throw new ParamsIllegalException('密码必须由字母和数字组成,长度应在6位以上')
        }
        user.salt = ValidationUtils.secureRandomSalt
        user.passwordHash = springSecurityService.encodePassword(newPwd, user.salt)
        user.save(flush: true, failOnError: true)
    }

    def resetPassword(User user) {
        String password = NumberUtils.genRandomCode(6)
        user.salt = ValidationUtils.secureRandomSalt
        user.passwordHash = springSecurityService.encodePassword(password, user.salt)
        user.save(flush: true, failOnError: true)
        password
    }

    def isSuperAdmin(Long userId) {
        if (!userId) {
            return false
        }

        def userRole = UserRole.createCriteria().get {
            user {
                eq('id', userId)
            }
            role {
                eq('authority', grailsApplication.config.getProperty("user.rootRole.name").toString())
            }
        }
        userRole ? true : false
    }

    def save(params){
        User user = new User(params)
        user.salt = ValidationUtils.secureRandomSalt
        user.passwordHash = DEFAULT_PASSWORD
        user.save(flush: true, failOnError: true)
        if (params.roles) {
            params.roles.each{roleId->
                UserRole.create user, Role.get(roleId),true
            }
        }
    }

    def update(User userInstance,params){
        userInstance.properties = params
        userInstance.save(flush: true, failOnError: true)

        if(!params.roles){
            UserRole.removeAll userInstance,true
            return
        }

        def userRoles=UserRole.findAllByUser(userInstance)

        def deleteUserRoleIds=userRoles?.role?.id-params.roles

        userRoles.findAll{UserRole userRole->
            userRole.role.id in deleteUserRoleIds
        }?.each {UserRole obj->
            obj.delete(flush: true)
        }

        (params.roles-userRoles?.id).each{roleId->
            UserRole.create userInstance, Role.get(roleId),true
        }
    }

    def delete(User userInstance){
        UserRole.removeAll(userInstance,true)
        userInstance.delete(flush: true)
    }

}