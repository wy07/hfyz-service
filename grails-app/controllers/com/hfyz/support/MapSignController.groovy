package com.hfyz.support

import com.commons.utils.ControllerHelper

class MapSignController implements ControllerHelper{

    def index() { }

    def list(){
        def mapSignList = MapSign.list([max:request.JSON.max, offset:request.JSON.offset, sort: "id"])?.collect { MapSign obj ->
            [  id  : obj.id
             , name: obj.name
             , mapSignType: obj.mapSignType.name
             , longitude: obj.longitude
             , latitude: obj.latitude
             , display: obj.display ? '显示':'隐藏']
        }
        renderSuccessesWithMap([mapSignList: mapSignList, total: MapSign.count()])
    }

    def delete() {
        withMapSign(params.long('id')) { mapSignInstance ->
            mapSignInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def changeDisplay() {
        withMapSign(params.long('id')) { mapSignInstance ->
            mapSignInstance.properties = request.JSON
            mapSignInstance.save(flush: true, failOnError: true)
            renderSuccess()
        }
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
