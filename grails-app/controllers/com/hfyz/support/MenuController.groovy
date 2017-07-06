package com.hfyz.support

import com.commons.utils.ControllerHelper
import com.commons.utils.NumberUtils
import grails.converters.JSON

class MenuController implements ControllerHelper {
    def supportService

    def list() {
        def menuList = supportService.getMenuTypeListByParent(params.long('parentId'))
        renderSuccessesWithMap([menuList: menuList])
    }

    def search() {
        if (!params.query) {
            renderSuccessesWithMap([menuList: []])
        }

        if (!params.position) {
            renderErrorMsg("请选择菜单位置")
        }

        def menuList = Menu.findAllByPositionAndCodeLike(params.position, "${params.query}%", [max: 30, sort: 'id', order: 'desc'])?.collect { Menu obj ->
            [id    : obj.id
             , name: obj.name
             , code: obj.code]
        }

        renderSuccessesWithMap([menuList: menuList])
    }

    def save() {

        Menu menu = new Menu(request.JSON)
        if (request.JSON?.parentId) {
            Long parentId = NumberUtils.toLong(request.JSON.parentId)
            def parent = parentId ? Menu.get(parentId) : null
            if (!parent) {
                renderErrorMsg('上层菜单不存在，请稍后再试！')
                return
            }
            menu.parent = parent
        }
        menu.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withMenu(params.long('id')) { menu ->
            renderSuccessesWithMap([menu    : [name       : menu.name
                                               , code     : menu.code
                                               , style    : menu.style
                                               , icon     : menu.icon
                                               , position : menu.position
                                               , display  : menu.display
                                               , id       : menu.id]
                                    , parent: menu.parent ? [name  : menu.parent.name
                                                             , code: menu.parent.code
                                                             , id  : menu.parent.id] : null])
        }
    }

    def update() {
        withMenu(params.long('id')) { menuInstance ->

            menuInstance.properties = request.JSON

            if (request.JSON?.parentId) {
                Long parentId = NumberUtils.toLong(request.JSON.parentId)
                def parent = parentId ? Menu.get(parentId) : null
                if (!parent) {
                    renderErrorMsg('上层菜单不存在，请稍后再试！')
                    return
                }
                menuInstance.parent = parent
            }
            menuInstance.save(flush: true, failOnError: true)

        }
        renderSuccess()
    }

    def delete() {
        withMenu(params.long('id')) { menuInstance ->
            menuInstance.delete(flush: true)
            renderSuccess()
        }
    }


    private withMenu(Long id, Closure c) {
        Menu menuInstance = id ? Menu.get(id) : null

        if (menuInstance) {
            c.call menuInstance
        } else {
            renderNoTFoundError()
        }
    }
}
