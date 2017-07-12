package com.hfyz.support

import com.commons.support.SystemCodeType
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll
import grails.buildtestdata.mixin.Build

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(SystemCodeController)
@Build([SystemCode, LicenseType])
class SystemCodeControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    @Unroll
    def "list:验证数据字典列表查询，传入type，当type为空或者为空字符串时：返回提示信息"() {
        when:
        params.type = type
        controller.list()
        then:
        response.status == 400
        response.json.errors == ['请求参数不合法，请查证！']
        where:
        type << [null, '']
    }

    def "list:输入相应的parentId和type,返回list"() {
        setup:
        controller.supportService = [getSystemCodeListByParent: { parentId, type ->
            ['id': 1, 'codeNum': 2, 'name': 'aa', 'type': '1', 'parentId': '1']
        }]
        SystemCodeType.instance.types = [unit: [name: 'unit', clazz: 'clazzObj', type: 'unit']]
        when:
        params.parentId = '1'
        params.type = 'unit'
        controller.list()
        then:
        response.status == 200
        response.json.systemCodeList == ['id': 1, 'codeNum': 2, 'name': 'aa', 'type': '1', parentId: '1']
        response.json.result == 'success'
    }

    def 'list:clazzObj为空,返回错误信息'() {
        when:
        params.type = null
        controller.list()
        then:
        response.json.errors == ['请求参数不合法，请查证！']
    }
}