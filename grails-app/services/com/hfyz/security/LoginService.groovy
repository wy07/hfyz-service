package com.hfyz.security

import grails.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import com.commons.exception.RecordNotFoundException
import com.commons.exception.ParamsIllegalException
import com.commons.utils.ValidationUtils


@Transactional
class LoginService {

    def authenticationManager
    def springSecurityService


    def signIn(username, password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password)
        Authentication authentication = authenticationManager.authenticate(token)
        return authentication.getPrincipal()
    }

    def getCurrentUserId(Long userId) {
        User user = userId ? User.get(userId) : null
        if (!user) {
            throw new RecordNotFoundException()
        }
        user
    }

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
        user.passwordHash = newPwd
        user.salt = ValidationUtils.secureRandomSalt
        user.save(flush: true, failOnError: true)
    }
}