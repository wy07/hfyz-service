package com.hfyz.support

import com.commons.utils.ControllerHelper
import com.commons.utils.ConfigUtil
import com.commons.utils.PageUtils

class ConfigureController implements ControllerHelper{

    def list(){
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def configureList = Configure.list([max:max, offset:offset, sort: 'id'])?.collect { Configure obj ->
            [ id  : obj.id
              , name: obj.name
              , configValue: obj.configValue
              , note: obj.note]
        }
        renderSuccessesWithMap([configureList: configureList, total: Configure.count()])
    }

    def edit() {
        withConfigure(params.long('id')) { configure ->
                renderSuccessesWithMap([configure: [id            : configure.id
                                                   , name         : configure.name
                                                   , configValue  : configure.configValue
                                                   , note         : configure.note]])
        }
    }

    def update() {
        withConfigure(params.long('id')) { configure ->
            configure.name = request.JSON.name
            configure.configValue = request.JSON.configValue
            configure.note = request.JSON.note
            configure.save(flush: true, failOnError: true)
            ConfigUtil.reLoadConfig()
        }
        renderSuccess()
    }

    private withConfigure(Long id, Closure c) {
        Configure configureInstance = id ? Configure.get(id) : null
        if (configureInstance) {
            c.call configureInstance
        } else {
            renderNoTFoundError()
        }
    }
}
