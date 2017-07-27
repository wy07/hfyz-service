package com.hfyz.security

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(SysuserController)
@Build([User])
class SysuserControllerSpec extends Specification{


    def "resetPassword:重置密码；当参数为空时，返回错误状态"(){

        when:
            request.JSON.id = null
            controller.resetPassword()

        then:
            response.status == 404
            response.json.errors == ['找不到您请求的数据，请查正！']

    }

    def "resetPassword:重置密码；当参数不为空时，返回重置之后的密码"(){
        setup:
            controller.userService = [resetPassword:{userInstanc->
                    return '666666' }]
        def userInstanc = User.build(id:1,passwordHash: '333333' )

        when:
            request.JSON.id = userInstanc.id
            controller.resetPassword()

        then:
            response.status == 200
            response.json.newPassword == '666666'

    }
}
