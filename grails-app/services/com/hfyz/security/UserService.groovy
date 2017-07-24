package com.hfyz.security

import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
import com.commons.utils.NumberUtils
import com.commons.utils.ValidationUtils

import grails.transaction.Transactional

@Transactional
class UserService {

    def springSecurityService

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


}