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

    def "index:当type为null，返回结果 "() {
        when:
        SystemCodeType.instance.types = null
        controller.index()
        then:
        response.json.systemCodeList == null
        response.json.type == null

    }
    def "index:当type存在时，返回相应结果"() {
        setup:
        controller.supportService = [getSystemCodeListByParent: { parentId, type ->
            ['id': 1, 'codeNum': 2, 'name': 'aa', 'type': '1', 'parentId': null]
        }]
        when:
        SystemCodeType.instance.types =  [unit: [name: 'unit', clazz: 'clazzObj', type: 'unit']]
        controller.index()
        then:
        response.json.systemCodeList ==['id': 1, 'codeNum': 2, 'name': 'aa', 'type': '1', 'parentId': null]
        response.json.type ==  'unit'
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
        request.JSON.type='unit'
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
    def "search:搜索数据字典，输入正确的参数， 返回保存成功的提示 "(){
        setup:
        def licenseType1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        when:
        SystemCodeType.instance.types=[LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        request.JSON.type='LICENSE_TYPE'
        request.JSON.query='aa'
        controller.search()
        then:
        response.status==200
        response.json.result=="success"
    }
    def "Search:搜索数据字典，传入type，当type为空或者为空字符串时：返回提示信息"() {
        when:
        request.JSON.type=type
        request.JSON.query='1'
        controller.search()
        then:
        response.status == 400
        response.json.errors == ['请求参数不合法，请查证！']
        where:
        type << [null, '']
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

    def "save: 验证保存，输入参数合理，在父类存在的情况下，保存子类，返回成功信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def licenseType1=LicenseType.build(name:'aa',codeNum: 'aa')
        when:
        params.type = 'LICENSE_TYPE'
        request.method = 'POST'
        request.JSON = [id: '2', codeNum: 'aa', name: 'aa', type: 'LICENSE_TYPE', parentId:licenseType1.id]
        controller.save()
        then:
        response.status == 200
        response.json.result == 'success'

    }
    def "save: 验证保存，输入参数合理，保存子类，返回成功信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        when:
        params.type = 'LICENSE_TYPE'
        request.method = 'POST'
        request.JSON = [id: '2', codeNum: 'aa', name: 'aa', type: 'LICENSE_TYPE']
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

    def "edit:验证数据字典保存，当type为空或者为空字符串时：返回提示信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
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

    }
    def "edit:验证数据字典保存，当id为空或者为空字符串时：返回提示信息"() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        when:
        request.JSON.type=type
        params.id = id
        controller.edit()
        then:
        response.status == status
        response.json.errors == errors
        where:
        id   | type           | status | errors
        null | 'LICENSE_TYPE' | 404    | ['找不到您请求的数据，请查正！']
        ''   | 'LICENSE_TYPE' | 404    | ['找不到您请求的数据，请查正！']

    }

    def "edit:验证编辑，输入参数正确,编辑已存在的父类 返回编辑成功提示 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def licenseType1 = LicenseType.build(codeNum: 'aaa', name: 'aa')
        when:
        request.JSON.type= 'LICENSE_TYPE'
        params.type = 'LICENSE_TYPE'
        params.id =licenseType1.id
        controller.edit()
        then:
        response.status == 200
        response.json.systemCode == [name: 'aa', codeNum: 'aaa', id: licenseType1.id]
        response.json.result == 'success'
    }

    def "edit: 验证编辑，传入参数正确,编辑一个子类 返回编辑成功提示 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def licenseType1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        def licenseType2 = LicenseType.build(codeNum: 'bb', name: 'bb', parent:licenseType1)
        when:
        request.JSON.type='LICENSE_TYPE'
        params.id = licenseType2.id
        controller.edit()
        then:
        response.status == 200
        response.json.parent == [codeNum: 'aa', name: 'aa', id: licenseType1.id]
        response.json.systemCode == [name: 'bb', codeNum: 'bb', id: licenseType2.id]
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
        def licenseType1 = LicenseType.build(codeNum: 'aaa', name: 'aa')
        when:
        params.id = licenseType1.id
        request.JSON.type='LICENSE_TYPE'
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
        request.JSON.type='LICENSE_TYPE'
         params.id = id
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
        def licenseType1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        def licenseType2 = LicenseType.build(codeNum: 'bb', name: 'bb')
        when:
        params.type = 'LICENSE_TYPE'
        params.id = licenseType2.id
        request.JSON = [name: 'cc', codeNum: 'cc', type: 'LICENSE_TYPE', parentId:licenseType1.id]
        controller.update()
        then:
        response.status == 200
        response.json.result == 'success'
    }

    def "update:验证更新，如果parentId和子类自己的Id相等,返回错误消息 "() {
        setup:
        SystemCodeType.instance.types = [LICENSE_TYPE: [name: 'LicenseType', clazz: LicenseType, type: 'LICENSE_TYPE']]
        def licenseType1 = LicenseType.build(codeNum: 'aa', name: 'aa')
        when:
        params.type = 'LICENSE_TYPE'
        params.id = licenseType1.id
        request.JSON = [name: 'bb', codeNum: 'bb', type: 'LICENSE_TYPE', parentId: licenseType1.id]
        controller.update()
        then:
        response.status == 400
        response.json.errors == ['数据非法，请稍后再试！']
    }

    def "getMenu:获取所有的菜单，返回正确的消息"() {
        setup:
        controller.supportService=[getMenu: {a->
            [menu:'menu']}]
        when:
       request.JSON.id=1
        controller.getmenu()
        then:
        response.status==200
        response.json.menu=='menu'
        response.json.result=='success'

    }
}

