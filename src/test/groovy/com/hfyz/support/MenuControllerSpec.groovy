package com.hfyz.support

import grails.buildtestdata.mixin.Build
import grails.test.mixin.Mock
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
            response.json.result   == 'success'

        where:
            parentId | menuList                                                                                                                          | list
            null     | [['data':['id':2,'code':'closeall','icon':null,'name':'关闭全部','style':null,'position':'TOP_BAR','display':true],'leaf':true]]  | [['data':['id':2,'code':'closeall','icon':null,'name':'关闭全部','style':null,'position':'TOP_BAR','display':true], 'leaf':true]]
            3        | [['data':['id':4,'code':'changepwd','icon':null,'name':'修改密码','style':null,'position':'TOP_BAR','display':true],'leaf':true]] | [['data':['id':4,'code':'changepwd','icon':null,'name':'修改密码','style':null,'position':'TOP_BAR','display':true],'leaf':true]]
    }

    def "search:搜索菜单，当入参不合法：query为null时，返回提示信息"() {
        when:
            params.query = null
            params.position = 'TOP_BAR'
            controller.search()

        then:
            response.json.menuList == []
            response.json.result   == 'success'
            response.json.errors   == null
    }

    def "search:搜索菜单，当入参不合法：position为null时，返回提示信息"() {
        when:
            params.query = 'ch'
            params.position = null
            controller.search()

        then:
            response.json.menuList == null
            response.json.result   == null
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
            response.json.menuList == list
            response.json.result   == result

        where:
            query | position   | result    | list
            'a'   | 'TOP_BAR'  | 'success' | [[code:'a', name:'b', id:3], [code:'ab', name:'a', id:2], [code:'abc', name:'abc', id:1]]
            'b'   | 'SIDE_BAR' | 'success' | [[code:'bc', name:'b', id:6]]
            'a'   | 'SIDE_BAR' | 'success' | [[code:'ac', name:'ac', id:5]]
            'b'   | 'TOP_BAR'  | 'success' | [[code:'b', name:'b', id:4]]
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
}
