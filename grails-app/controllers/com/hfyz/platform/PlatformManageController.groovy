package com.hfyz.platform

import com.commons.utils.ControllerHelper

class PlatformManageController implements ControllerHelper {


    def platformManageService

    def list() {
        renderSuccessesWithMap([platformList: platformManageService.getPlatformList(request.JSON.max, request.JSON.offset, request.JSON.name, request.JSON.code)])
    }

//    添加
    def save() {
        PlatformManage platformInstance = new PlatformManage(request.JSON)
        platformInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }
//    删除

    def delete() {
        withPlatform(params.long('id')) { platformInstance ->
            platformInstance.delete(flush: true)
            renderSuccess()
        }
    }

//    编辑
    def edit() {
        withPlatform(params.long('id')) { platform ->
            renderSuccessesWithMap([platform: [id            : platform.id
                                               , ip          : platform.ip
                                               , port        : platform.port
                                               , name        : platform.name
                                               , code        : platform.code
                                               , contactName : platform.contactName
                                               , contactPhone: platform.contactPhone]])

        }
    }
    def update(){
        withPlatform(params.long('id')) { platformInstance ->
            platformInstance.properties = request.JSON
            platformInstance.save(flush: true, failOnError: true)
            renderSuccess()

        }
    }

    private withPlatform(Long id, Closure c) {
        PlatformManage platformInstance = id ? PlatformManage.get(id) : null
        if (platformInstance) {
            c.call platformInstance
        } else {
            renderNoTFoundError()
        }

    }

}
