package com.hfyz.support

import com.commons.utils.ControllerHelper

class MapSignController implements ControllerHelper {

    def index() {}

    def list() {
        def mapSignList = MapSign.list([max: request.JSON.max, offset: request.JSON.offset, sort: "id", order: 'desc'])?.collect { MapSign obj ->
            [id           : obj.id
             , name       : obj.name
             , mapSignType: obj.mapSignType.name
             , longitude  : obj.longitude
             , latitude   : obj.latitude
             , display    : obj.display]
        }
        renderSuccessesWithMap([mapSignList: mapSignList, total: MapSign.count()])
    }

    def save() {
        MapSign mapSign = new MapSign(request.JSON)
        mapSign.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withMapSign(params.long('id')) { MapSign mapSign ->
            renderSuccessesWithMap([mapSign: [id         : mapSign.id
                                              , name     : mapSign.name
                                              , longitude: mapSign.longitude
                                              , latitude : mapSign.latitude
                                              , display  : mapSign.display
                                              , typeId   : mapSign.mapSignType?.id]])
        }
    }

    def update() {
        withMapSign(params.long('id')) { MapSign mapSign ->
            mapSign.properties = request.JSON
            mapSign.save(flush: true, failOnError: true)
            renderSuccess()
        }

    }

    def delete() {
        withMapSign(params.long('id')) { mapSignInstance ->
            mapSignInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def changeDisplay() {
        withMapSign(params.long('id')) { mapSignInstance ->
            if (request.JSON.display != mapSignInstance.display) {
                mapSignInstance.display = request.JSON.display
                mapSignInstance.save(flush: true, failOnError: true)
            }
        }
        renderSuccess()
    }

    def typeList() {
        def typeList = MapSignType.list([sort: 'id', order: 'desc']).collect { MapSignType obj ->
            [value: obj.id, label: obj.name, code: obj.codeNum]
        }
        renderSuccessesWithMap([typeList: typeList])
    }

    private withMapSign(Long id, Closure c) {
        MapSign mapSignInstance = id ? MapSign.get(id) : null

        if (mapSignInstance) {
            c.call mapSignInstance
        } else {
            renderNoTFoundError()
        }
    }
}
