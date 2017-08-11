package com.hfyz.support

import com.commons.support.SystemCodeType
import com.commons.utils.ControllerHelper
import com.commons.utils.KafkaDataUtils
import com.commons.utils.LogUtils
import com.commons.utils.NumberUtils
import com.hfyz.workOrder.WorkOrder

class SystemCodeController implements ControllerHelper {

    def supportService

    def index() {
        def systemCodeTypes = SystemCodeType.instance.systemCodeTypes.keySet()
        def type = systemCodeTypes?.first()
        def systemCodeList = []
        if (type) {
            systemCodeList = supportService.getSystemCodeListByParent(null, type)
        }
        renderSuccessesWithMap([systemCodeTypes : systemCodeTypes
                                , systemCodeList: systemCodeList
                                , type          : type])

    }

    def list() {
        def clazzObj = getClazzObj(request.JSON.type)
        if (!clazzObj) {
            renderParamsIllegalErrorMsg()
            return
        }
        def systemCodeList = supportService.getSystemCodeListByParent(NumberUtils.toLong(request.JSON.parentId), clazzObj.type)

        renderSuccessesWithMap([systemCodeList: systemCodeList])
    }

    def search() {
        if (!request.JSON.query) {
            renderSuccessesWithMap([systemCodeList: []])
        }

        def clazzObj = getClazzObj(request.JSON.type)
        if (!clazzObj) {
            renderParamsIllegalErrorMsg()
            return
        }

        def systemCodeList = clazzObj.clazz.findAllByCodeNumLike("${request.JSON.query}%", [max: 30, sort: 'id', order: 'desc'])?.collect { obj ->
            [
                    id       : obj.id
                    , name   : obj.name
                    , codeNum: obj.codeNum]
        }
        renderSuccessesWithMap([systemCodeList: systemCodeList])
    }

    def save() {
        def clazz = getClazzObj(request.JSON.type)?.clazz
        if (!clazz) {
            renderParamsIllegalErrorMsg()
            return
        }

        def clazzInstance = clazz.newInstance()
        clazzInstance.properties = request.JSON

        if (request.JSON?.parentId) {
            Long parentId = NumberUtils.toLong(request.JSON.parentId)
            def parent = parentId ? clazz.get(parentId) : null
            if (!parent) {
                renderErrorMsg('上级数据不存在，请稍后再试！')
                return
            }
            clazzInstance.parent = parent
        }
        clazzInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withSystemCode(params.long('id'), request.JSON.type) { sys, clazz ->
            renderSuccessesWithMap([systemCode: [name     : sys.name
                                                 , codeNum: sys.codeNum
                                                 , id     : sys.id]
                                    , parent  : sys.parent ? [name     : sys.parent.name
                                                              , codeNum: sys.parent.codeNum
                                                              , id     : sys.parent.id] : null])


        }
    }

    def update() {
        withSystemCode(params.long('id'), request.JSON.type) { systemCodeInstance, clazz ->

            systemCodeInstance.properties = request.JSON

            if (request.JSON?.parentId) {
                Long parentId = NumberUtils.toLong(request.JSON.parentId)
                def parent = parentId ? clazz.get(parentId) : null
                if (!parent) {
                    renderErrorMsg('上级数据不存在，请稍后再试！')
                    return
                }
                if(systemCodeInstance==parent){
                    renderErrorMsg('数据非法，请稍后再试！')
                    return
                }
                systemCodeInstance.parent = parent
            }
            systemCodeInstance.save(flush: true, failOnError: true)

        }
        renderSuccess()
    }

    def delete() {
        withSystemCode(params.long('id'), request.JSON.type) { systemCodeInstance, clazz ->
            systemCodeInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def getmenu() {

        LogUtils.debug(this.class, params, request)
        renderSuccessesWithMap(supportService.getMenu())
    }

    private withSystemCode(Long id, String type, Closure c) {
        def clazz = getClazzObj(type)?.clazz
        if (!clazz) {
            renderParamsIllegalErrorMsg()
            return
        }

        def systemCodeInstance = id ? clazz.get(id) : null

        if (systemCodeInstance) {
            c.call systemCodeInstance, clazz
        } else {
            renderNoTFoundError()
        }
    }

    private getClazzObj(String type) {
        if (!type) {
            return null
        }
        return SystemCodeType.instance.systemCodeTypes?.get(type)
    }
}
