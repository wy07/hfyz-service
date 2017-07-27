package com.hfyz.security

import com.commons.utils.NumberUtils
import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(UserService)
@Build([User])
@Mock(SpringSecurityService)
class UserServiceSpec extends Specification {

    def setup(){

    }

    def cleanup(){

    }

    def "resetPassword:修改密码，返回随机生成的生成的6为数密码"(){
        setup:
        def user = User.build(id:1,name:'aa',username:'lisi',salt:'222222')
        NumberUtils.metaClass.static.genRandomCode = {int n-> return '111111' }
        service.springSecurityService = [encodePassword:{String pass,String alt  -> return '123456'}]

        when:
        println("user"+user.salt)
        println "${NumberUtils.genRandomCode(6)}"
        def password = service.resetPassword(user)

        then:
        password == '111111'

    }
}
