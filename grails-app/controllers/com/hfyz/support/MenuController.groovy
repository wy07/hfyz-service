package com.hfyz.support

import com.commons.utils.ControllerHelper
import com.commons.utils.NumberUtils
import grails.converters.JSON

class MenuController implements ControllerHelper {
    def supportService

    def list() {
        def menuList = supportService.getMenuTypeListByParent(NumberUtils.toLong(request.JSON.parentId))
        renderSuccessesWithMap([menuList: menuList])
    }

    def search() {
        if (!request.JSON.query) {
            renderSuccessesWithMap([menuList: []])
            return
        }

        if (!request.JSON.position) {
            renderErrorMsg("请选择菜单位置")
            return
        }

        def menuList = Menu.findAllByPositionAndCodeLike(request.JSON.position, "${request.JSON.query}%", [max: 30, sort: 'id', order: 'desc'])?.collect { Menu obj ->
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
            Menu child=Menu.findByParent(menuInstance)
            if(child){
                  renderErrorMsg('当前菜单下有子菜单,不能删除！')
                  return
            }
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
