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

    def "index:验证SystemCodeType为空和不为空时返回不同结果 "() {
        setup:
        controller.supportService = [getSystemCodeListByParent: { parentId, type ->
            ['id': 1, 'codeNum': 2, 'name': 'aa', 'type': '1', 'parentId': null]
        }]
        when:
        SystemCodeType.instance.types = types
        controller.index()
        then:
        response.json.systemCodeList == list
        response.json.type == type
        where:
        types                                                   | type   | list
        null                                                    | null   | null
        [unit: [name: 'unit', clazz: 'clazzObj', type: 'unit']] | 'unit' | ['id': 1, 'codeNum': 2, 'name': 'aa', 'type': '1', 'parentId': null]
    }

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

    def "list:验证数据字典列表查询，输入合法的参数，返回正确结果"() {
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

    def 'list:验证clazzObj为空,返回错误消息'() {
        when:
        params.type = null
        controller.list()
        then:
        response.json.errors == ['请求参数不合法，请查证！']
    }

    def "search:搜索数据字典，当入参不合法，query为null时，返回提示信息"() {
        when:
        params.query = null
        controller.search()
        then:
        response.status == 200
        response.json.systemCodeList == []
        response.json.result == 'success'
    }

    def "Search:搜索数据字典，传入type，当type为空或者为空字符串时：返回提示信息"() {
        when:
        params.type = type
        params.query = query
        controller.search()
        then:
        response.status == 400
        response.json.errors == ['请求参数不合法，请查证！']
        where:
        type | query | _
        null | '1'   | _
        ''   | '1'   | _
    }

    def "search:搜索数据词典，当入参合法：#query,返回正确结果"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        controller.supportService = [searchSystemCode: { clazzObj, query ->
            ['id': '1', 'codeNum': 'aa', 'name': 'aa', 'type': '1', 'parentId': '1']
        }]
        when:
        params.type = 'LICENSE_TYPE'
        params.query = 'aa'
        controller.search()
        then:
        response.status == 200
        response.json.systemCodeList == ['id': '1', 'codeNum': 'aa', 'name': 'aa', 'type': '1', 'parentId': '1']
        response.json.result == 'success'
    }

    def "save:验证数据字典保存，传入type，当type为空或者为空字符串时：返回提示信息"() {
        when:
        params.type = type
        controller.save()
        then:
        response.status == 400
        response.json.errors == ['请求参数不合法，请查证！']
        where:
        type << [null, '']
    }

    def "save: 验证保存，输入参数合理，在父类存在的情况下，保存子类，返回提示信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1=LicenseType.build(name:'aa',codeNum: 'aa')
        when:
        params.type = 'LICENSE_TYPE'
        request.method = 'POST'
        request.JSON = [id: '2', codeNum: 'aa', name: 'aa', type: 'LICENSE_TYPE', parentId:s1.id]
        controller.save()
        then:
        response.status == 200
        response.json.result == 'success'

    }
    def "save: 验证保存，父类不存在，保存子类，返回提示信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        when:
        params.type = 'LICENSE_TYPE'
        request.method = 'POST'
        request.JSON = [id: '1', codeNum: 'aa', name: 'aa', type: 'LICENSE_TYPE',parentId:2]
        controller.save()
        then:
        response.status == 400
        response.json.errors == ['上级数据不存在，请稍后再试！']

    }

    def "edit:验证数据字典保存，传入type，当type为空或者为空字符串时：返回提示信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        when:
        params.type = type
        params.id = id
        controller.edit()
        then:
        response.status == status
        response.json.errors == errors
        where:
        id   | type           | status | errors
        null | ''             | 400    | ['请求参数不合法，请查证！']
        null | null           | 400    | ['请求参数不合法，请查证！']
        null | 'LICENSE_TYPE' | 404    | ['找不到您请求的数据，请查正！']
        ''   | 'LICENSE_TYPE' | 404    | ['找不到您请求的数据，请查正！']

    }

    def "edit:验证编辑，输入参数正确,编辑已存在的父类 返回编辑成功提示 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1 = LicenseType.build(codeNum: 'aaa', name: 'aa')
        when:
        params.type = 'LICENSE_TYPE'
        params.id = s1.id
        controller.edit()
        then:
        response.status == 200
        response.json.systemCode == [name: 'aa', codeNum: 'aaa', id: s1.id]
        response.json.result == 'success'
    }

    def "edit:  参数正确,编辑一个子类 返回编辑成功提示 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        def s2 = LicenseType.build(codeNum: 'bb', name: 'bb', parent: s1)
        when:
        params.type = 'LICENSE_TYPE'
        params.id = s2.id
        controller.edit()
        then:
        response.status == 200
        response.json.parent == [codeNum: 'aa', name: 'aa', id: s1.id]
        response.json.systemCode == [name: 'bb', codeNum: 'bb', id: s2.id]
        response.json.result == 'success'
    }

    def "delete:验证数据字典列表删除，传入type，当type为空或者为空字符串时：返回提示信息"() {
        when:
        params.type = type
        controller.delete()
        then:
        response.status == 400
        response.json.errors == ['请求参数不合法，请查证！']
        where:
        type << [null, '']
    }

    def "delete:验证删除，输入正确的参数id,type，返回提示信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1 = LicenseType.build(codeNum: 'aaa', name: 'aa')
        when:
        params.id = s1.id
        params.type = 'LICENSE_TYPE'
        controller.delete()
        then:
        response.status == 200
        response.json.result == 'success'
    }

    def "update:验证更新，传入type,当type为空时或者为空字符串时，返回错误消息"() {
        when:
        params.type = type
        controller.update()
        then:
        response.status == 400
        response.json.errors == ["请求参数不合法，请查证！"]
        where:
        type << [null, '']
    }

    def "update:验证更新,type存在，传入id,当id为空时或为空字符串，返回错误消息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        when:
        params.id = id
        params.type = 'LICENSE_TYPE'
        controller.update()
        then:
        response.status == 404
        response.json.errors == ['找不到您请求的数据，请查正！']
        where:
        id << [null, '']
    }

    def "update:验证更新，参数正确，返回正确消息 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        def s2 = LicenseType.build(codeNum: 'bb', name: 'bb')
        when:
        params.type = 'LICENSE_TYPE'
        params.id = s2.id
        request.JSON = [name: 'cc', codeNum: 'cc', type: 'LICENSE_TYPE', parentId: s1.id]
        controller.update()
        then:
        response.status == 200
        response.json.result == 'success'
    }

    def "update:验证更新，如果parentId和子类自己的Id相等,返回错误消息 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def s1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        when:
        params.type = 'LICENSE_TYPE'
        params.id = s1.id
        request.JSON = [name: 'bb', codeNum: 'bb', type: 'LICENSE_TYPE', parentId: s1.id]
        controller.update()
        then:
        response.status == 400
        response.json.errors == ['数据非法，请稍后再试！']
    }

    def "getMenu:获取所有的菜单，返回正确的消息"() {
        setup:
        controller.supportService=[getMenu: {a->
            [s:'s']}]
        when:
        params.id=1
        controller.getmenu()
        then:
        response.status==200
        response.json.result=='success'

    }
}

