package com.hfyz.support

import grails.buildtestdata.mixin.Build
import grails.converters.JSON
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
            response.json.menuList == list
            response.json.result   == result

        where:
            parentId  | result    | menuList                                                                                                                          | list
            null      | 'success' | [['data':['id':2,'code':'closeall','icon':null,'name':'关闭全部','style':null,'position':'TOP_BAR','display':true],'leaf':true]]  | [['data':['id':2,'code':'closeall','icon':null,'name':'关闭全部','style':null,'position':'TOP_BAR','display':true], 'leaf':true]]
            3         | 'success' | [['data':['id':4,'code':'changepwd','icon':null,'name':'修改密码','style':null,'position':'TOP_BAR','display':true],'leaf':true]] | [['data':['id':4,'code':'changepwd','icon':null,'name':'修改密码','style':null,'position':'TOP_BAR','display':true],'leaf':true]]
    }

    @Unroll
    def "search:搜索菜单，当入参不合法：#query,#position，返回不同的结果"() {
        when:
            params.query = query
            params.position = position
            controller.search()

        then:
            response.json.menuList == list
            response.json.result   == result
            response.json.errors   == errors

        where:
            query | position  | MenuList                                | result    | errors            | list
            null  | null      | []                                      | 'success' | null              | []
            'ch'  | null      | null                                    | null      | ['请选择菜单位置'] | null
            null  | 'TOP_BAR' | []                                      | 'success' | null              | []
    }


    @Unroll
    def "search:搜索菜单，当入参合法：#query,#position，返回正确结果"(){
        setup:
            Menu.build(name: 'abc', code:'abc', position:'TOP_BAR')
            Menu.build(name: 'a',   code: 'ab', position:'TOP_BAR')
            Menu.build(name: 'b',   code: 'a',  position:'TOP_BAR')
            Menu.build(name: 'b',   code: 'b',  position:'TOP_BAR')
            Menu.build(name: 'b',   code: 'bc', position:'SIDE_BAR')
            Menu.build(name: 'c',   code: 'c',  position:'SIDE_BAR')

        when:
            params.query = query
            params.position = position
            controller.search()

        then:
            response.json.menuList == list
            response.json.result   == result

        where:
            query | position   | result    | list
            'a'   | 'TOP_BAR'  | 'success' | [[code:'a', name:'b', id:3], [code:'ab', name:'a', id:2], [code:'abc', name:'abc', id:1]]
            'b'   | 'SIDE_BAR' | 'success' | [[code:'bc', name:'b', id:5]]
    }

    def "search:搜索菜单，当入参合法，返回最多max:30条数据"(){
        setup:
            50.times {
                Menu.build(name: 'abc', code: 'abc', position: 'TOP_BAR')
            }
        when:
            params.query = query
            params.position = position
            controller.search()

        then:
            response.json.menuList.size() == count

        where:
            query | position  | count
            'a'   | 'TOP_BAR' | 30
    }

    @Unroll
    def "save:保存菜单，当入参合法:#json，返回success"(){
        setup:
            Menu.build(name: 'abc', code:'abc', position:'TOP_BAR')
            Menu.build(name: 'a',   code: 'ab', position:'TOP_BAR')

        when:
            request.method = 'POST'
            request.JSON = json
            controller.save()

        then:
            response.json.result == result
            Menu.count() == 3

        where:
            json                                                  | result
            [name: 'd', code:'d', position:'TOP_BAR', parentId:1] | 'success'
            [name: 'd', code:'d', position:'TOP_BAR']             | 'success'
    }

    @Unroll
    def "save:保存菜单，当入参不合法，返回提示信息:#errors"(){
        setup:
            Menu.build(name: 'abc', code:'abc', position:'TOP_BAR')
            Menu.build(name: 'a',   code: 'ab', position:'TOP_BAR')

        when:
            request.method = 'POST'
            request.JSON = json
            controller.save()

        then:
            response.json.errors == errors

        where:
            json                                                  | errors
            [name: 'd', code:'d', position:'TOP_BAR', parentId:3] | ['上层菜单不存在，请稍后再试！']
    }

    @Unroll
    def "edit:编辑菜单，当入参合法:#id，返回正确结果"() {
        setup:
            Menu.build(name: 'c', code: 'c')
            Menu.build(name: 'a', code: 'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')
            Menu.build(name: 'b', code: 'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: Menu.get(1))

        when:
            params.id = id
            controller.edit()

        then:
            response.json.menu   == menu
            response.json.parent == parent
            response.json.result == result

        where:
            id | result    | parent                     | menu
            2  | 'success' | null                       | [code:'a', display:true, name:'a', icon:'a', style:'a', position:'TOP_BAR', id:2]
            3  | 'success' | [code:'c', name:'c', id:1] | [code:'b', display:true, name:'b', icon:'b', style:'b', position:'TOP_BAR', id:3]
    }

    @Unroll
    def "edit:编辑菜单，当入参不合法:3，返回提示信息:#errors"() {
        setup:
            Menu.build(name: 'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')
            Menu.build(name: 'b', code:'b', style:'b', icon:'b', position:'TOP_BAR', display:'b')

        when:
            params.id = id
            controller.edit()

        then:
            response.json.errors == errors
        where:
            id | errors
            3  | ['找不到您请求的数据，请查正！']
    }

    @Unroll
    def "update:修改菜单，当入参合法时#id,#json，返回success"() {
        setup:
            Menu.build(name:'c', code:'c')
            Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')
            Menu.build(name:'b', code:'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: Menu.get(1))

        when:
            params.id = id
            request.method = 'POST'
            request.JSON = json
            controller.update()

        then:
            println Menu.get(id) as JSON
            response.json.result == result

        where:
            id | result    | json
            3  | 'success' | [name: 'd', code:'d', position:'TOP_BAR', parentId:2]
            3  | 'success' | [name: 'd', code:'d', position:'TOP_BAR']
            2  | 'success' | [name: 'd', code:'d', position:'TOP_BAR', parentId:2]
            2  | 'success' | [name: 'd', code:'d', position:'TOP_BAR']
    }

    @Unroll
    def "update:修改菜单，当入参不合法时#id,#json，返回提示信息:#errors"() {
        setup:
            Menu.build(name:'c', code:'c')
            Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')
            Menu.build(name:'b', code:'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: Menu.get(1))

        when:
            params.id = id
            request.method = 'POST'
            request.JSON = json
            controller.update()

        then:
            response.json.errors == errors

        where:
            id | errors                          | json
            4  | ['找不到您请求的数据，请查正！'] | [name: 'd', code:'d', position:'TOP_BAR', parentId:2]
            4  | ['找不到您请求的数据，请查正！'] | [name: 'd', code:'d', position:'TOP_BAR']
            3  | ['上层菜单不存在，请稍后再试！'] | [name: 'd', code:'d', position:'TOP_BAR', parentId:4]
            2  | ['上层菜单不存在，请稍后再试！'] | [name: 'd', code:'d', position:'TOP_BAR', parentId:4]
    }

    @Unroll
    def "delete:删除菜单叶子节点，当入参合法时#id，返回success"() {
        setup:
            Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')
            Menu.build(name:'b', code:'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: Menu.get(1))

        when:
            params.id = id
            println '--------------delete前---------' + Menu.count()
            println Menu.findAll() as JSON
            controller.delete()
            println '--------------delete后---------' + Menu.count()
            println Menu.findAll() as JSON

        then:
            println response.json
            println Menu.get(id) as JSON
            response.json.result == result

        where:
            id | result
            1  | 'success'
            2  | 'success'
    }

    @Unroll
    def "delete:删除菜单叶子节点，当入参不合法时#id，返回提示信息:#errors"() {
        setup:
            Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a')

        when:
            params.id = id
            controller.delete()

        then:
            response.json.errors == errors

        where:
            id | errors
            2  | ['找不到您请求的数据，请查正！']
    }

//    @Unroll
//    def "delete:删除菜单，当入参合法时#id，返回success"() {
//        setup:
//            Menu.build(name:'c', code:'c').save(flush:true)
//            Menu.build(name:'a', code:'a', style:'a', icon:'a', position:'TOP_BAR', display:'a').save(flush:true)
//            Menu.build(name:'b', code:'b', style:'b', icon:'b', position:'TOP_BAR', display:'b', parent: Menu.get(1)).save(flush:true)
//
//         when:
//            params.id = id
//            println '--------------delete前---------' + Menu.count()
//            println Menu.findAll() as JSON
//            controller.delete()
//            println '--------------delete后---------' + Menu.count()
//            println Menu.findAll() as JSON
//
//        then:
//            println response.json
//            println Menu.get(id) as JSON
//            response.json.result == result
//
//        where:
//            id | result
//            1  | 'success'
//    }
}
