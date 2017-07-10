package com.hfyz.support

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MenuController)
@Build([Menu])
class MenuControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    @Unroll
    def "list:验证菜单列表查询，输入不同的parentId:#parentId ，返回不同的列表"() {
        setup:
        controller.supportService = [getMenuTypeListByParent:{parentId->
                return menuList}]

        when:
        params.parentId = parentId
        controller.list()

        then:
        response.status        == 200
        response.json.menuList == list
        response.json.result   == 'success'

        where:
        parentId | menuList                                                                                                                          | list
        null     | [['data':['id':2,'code':'closeall','icon':null,'name':'关闭全部','style':null,'position':'TOP_BAR','display':true],'leaf':true]]  | [['data':['id':2,'code':'closeall','icon':null,'name':'关闭全部','style':null,'position':'TOP_BAR','display':true], 'leaf':true]]
        3        | [['data':['id':4,'code':'changepwd','icon':null,'name':'修改密码','style':null,'position':'TOP_BAR','display':true],'leaf':true]] | [['data':['id':4,'code':'changepwd','icon':null,'name':'修改密码','style':null,'position':'TOP_BAR','display':true],'leaf':true]]
    }

    def "search:搜索菜单，当入参不合法：query为null时，返回提示信息"() {
        when:
        params.query = null
        controller.search()

        then:
        response.status        == 200
        response.json.menuList == []
        response.json.result   == 'success'
    }

    def "search:搜索菜单，当入参不合法：position为null时，返回提示信息"() {
        when:
        params.query = 'ch'
        params.position = null
        controller.search()

        then:
        response.status        == 400
        response.json.menuList == null
        response.json.errors   == ['请选择菜单位置']
    }

    @Unroll
    def "search:搜索菜单，当入参合法：#query,#position，返回正确结果"(){
        setup:
        Menu.build(name: 'abc', code:'abc', position:'TOP_BAR')
        Menu.build(name: 'a',   code: 'ab', position:'TOP_BAR')
        Menu.build(name: 'b',   code: 'a',  position:'TOP_BAR')
        Menu.build(name: 'b',   code: 'b',  position:'TOP_BAR')
        Menu.build(name: 'ac',   code: 'ac',  position:'SIDE_BAR')
        Menu.build(name: 'b',   code: 'bc', position:'SIDE_BAR')
        Menu.build(name: 'c',   code: 'c',  position:'SIDE_BAR')

        when:
        params.query = query
        params.position = position
        controller.search()

        then:
        response.status        == 200
        response.json.menuList == list
        response.json.result   == 'success'

        where:
        query | position   | list
        'a'   | 'TOP_BAR'  | [[code:'a', name:'b', id:3], [code:'ab', name:'a', id:2], [code:'abc', name:'abc', id:1]]
        'b'   | 'SIDE_BAR' | [[code:'bc', name:'b', id:6]]
        'a'   | 'SIDE_BAR' | [[code:'ac', name:'ac', id:5]]
        'b'   | 'TOP_BAR'  | [[code:'b', name:'b', id:4]]
    }

    def "search:搜索菜单，当入参合法，返回最多max:30条数据"(){
        setup:
        50.times {
            Menu.build(name: 'abc', code: 'abc', position: 'TOP_BAR')
        }
        when:
        params.query = 'a'
        params.position = 'TOP_BAR'
        controller.search()

        then:
        response.json.menuList.size() == 30
    }

    def "save:保存菜单，当入参合法，返回success"(){
        setup:
        def m1 = Menu.build(name: 'abc', code:'abc', position:'TOP_BAR')

        when:
        request.method = 'POST'
        request.JSON = [name: 'd', code:'d', position:'TOP_BAR', parentId:m1.id]
        controller.save()

        then:
        response.status      == 200
        response.json.result == 'success'
        Menu.count()         == 2
    }

    def "save:保存菜单，当入参不合法:上层菜单不存在，返回提示信息"(){
        when:
        request.method = 'POST'
        request.JSON = [name: 'd', code:'d', position:'TOP_BAR', parentId:2]
        controller.save()

        then:
        response.status      == 400
        response.json.errors == ['上层菜单不存在，请稍后再试！']
    }

    def "save:保存菜单，当入参不合法：必填字段为空，返回错误信息"(){
        when:
        request.method = 'POST'
        request.JSON = [name: '', code:'d', position:'TOP_BAR']
        controller.save()

        then:
        response.status      == 400
        response.json.errors == ['Property [name] of class [class com.hfyz.support.Menu] cannot be null']
    }

    def "edit:编辑菜单，当入参合法,编辑无父节点菜单，返回正确结果"() {
        setup:
        def m1 = Menu.build(name: 'a', code: 'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')

        when:
        params.id = m1.id
        controller.edit()

        then:
        response.json.menu   == [code:'a', display:true, name:'a', icon:'a', style:'a', position:'TOP_BAR', id:m1.id]
        response.json.parent == null
        response.json.result == 'success'
    }

    def "edit:编辑菜单，当入参合法,编辑有父节点菜单，返回正确结果"() {
        setup:
        def m1 = Menu.build(name: 'c', code: 'c')
        def m2 = Menu.build(name: 'b', code: 'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: m1)

        when:
        params.id = m2.id
        controller.edit()

        then:
        response.json.menu   == [code:'b', display:true, name:'b', icon:'b', style:'b', position:'TOP_BAR', id:m2.id]
        response.json.parent == [code:'c', name:'c', id:m1.id]
        response.json.result == 'success'
    }

    def "edit:编辑菜单，当入参不合法:id不存在，返回提示信息"() {
        when:
        params.id = 1
        controller.edit()

        then:
        response.status      == 404
        response.json.errors == ['找不到您请求的数据，请查正！']
    }

    def "update:修改菜单，当入参合法时,修改有父节点菜单，返回success"() {
        setup:
        def m1 = Menu.build(name:'c', code:'c')
        def m2 = Menu.build(name:'b', code:'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: m1)

        when:
        params.id = m2.id
        request.method = 'POST'
        request.JSON = [name: 'd', code:'d', position:'TOP_BAR', parentId:m2.id]
        controller.update()

        then:
        response.status      == 200
        response.json.result == 'success'
    }

    def "update:修改菜单，当入参合法时修改无父节点菜单，返回success"() {
        setup:
        def m1 = Menu.build(name:'c', code:'c')
        def m2 = Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')
        when:
        params.id = m2.id
        request.method = 'POST'
        request.JSON = [name: 'd', code:'d', position:'TOP_BAR', parentId:m1.id]
        controller.update()

        then:
        response.status      == 200
        response.json.result == 'success'
    }

    def "update:修改菜单，当入参不合法时:无此id，返回提示信息"() {
        when:
        params.id = 1
        request.method = 'POST'
        request.JSON = [name: 'd', code:'d', position:'TOP_BAR']
        controller.update()

        then:
        response.status      == 404
        response.json.errors == ['找不到您请求的数据，请查正！']
    }

    def "update:修改菜单，当入参不合法时:必填字段name为空，返回提示信息"() {
        setup:
        def m1 = Menu.build(name:'c', code:'c',position:'TOP_BAR')

        when:
        params.id = m1.id
        request.method = 'POST'
        request.JSON = [name: '', code:'d', position:'TOP_BAR']
        controller.update()

        then:
        response.status      == 400
        response.json.errors == ['Property [name] of class [class com.hfyz.support.Menu] cannot be null']
    }

    def "update:修改菜单，当入参不合法时:上层菜单不存在，返回提示信息"() {
        setup:
        def m1 = Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')

        when:
        params.id = m1.id
        request.method = 'POST'
        request.JSON = [name: 'd', code:'d', position:'TOP_BAR', parentId:4]
        controller.update()

        then:
        response.status      == 400
        response.json.errors == ['上层菜单不存在，请稍后再试！']
    }

    def "delete:删除菜单叶子节点，当入参合法时#id，返回success"() {
        setup:
        def m1 = Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')

        when:
        params.id = m1.id
        controller.delete()

        then:
        response.json.result == 'success'
    }

    def "delete:删除不存在的节点，当入参不合法时，返回提示信息"() {
        when:
        params.id = 1
        controller.delete()

        then:
        response.status      == 404
        response.json.errors == ['找不到您请求的数据，请查正！']
    }
}
