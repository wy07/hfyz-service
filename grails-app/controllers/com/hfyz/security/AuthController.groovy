package com.hfyz.security

import com.commons.utils.ControllerHelper
import com.commons.utils.LogUtils
import grails.converters.JSON
import org.springframework.security.authentication.*
import org.springframework.security.core.userdetails.UserDetails

class AuthController implements ControllerHelper {

    def loginService

    def singIn() {
        def username = request.JSON?.username
        def password = request.JSON?.password

        if (!username || !password) {

            renderErrorMsg(message(code: 'login.usernamePwd.null.label', default: '用户名密码不能为空'))
        } else {
            try {
                UserDetails userDetails = loginService.signIn(username, password)

                def rights = springSecurityService.findRequestmapsByRoles(userDetails.authorities.role).code.join(';')




                renderSuccessesWithMap([sub:userDetails.username
                                        ,role:userDetails.authorities.authority.join(",")
                                        ,id: userDetails.id
                                        ,companyCode: userDetails.companyCode
                                        ,rights:rights])
            } catch (BadCredentialsException e) {
                renderErrorMsg(message(code: 'login.BadCredentials.label', default: '您的用户名和密码不匹配，请重新输入'))
            } catch (Exception e) {
                LogUtils.error(this.class, params, request, '登录', '用户', session, '首页登录', e.message)
                renderErrorMsg(message(code: 'default.systemBusy.message', default: '系统忙，请稍后再试'))
            }
        }

    }
}
