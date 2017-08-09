package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class FreightRouterController implements ControllerHelper {

    def freightRouterService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(freightRouterService.getListAndTotal(max, offset))
    }

    def save() {
        FreightRouter freightRouterInstance = new FreightRouter(request.JSON)
        freightRouterInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def delete() {
        withFreightRouter(params.long('id')) { freightRouterInstance ->
            freightRouterInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def edit() {
        withFreightRouter(params.long('id')) { freightRouterInstance ->
            renderSuccessesWithMap([freightRouter: [id            : freightRouterInstance.id
                                                    ,routerName: freightRouterInstance.routerName
                                                    ,startProvince: freightRouterInstance.startProvince
                                                    ,startCity: freightRouterInstance.startCity
                                                    ,startDistrict: freightRouterInstance.startDistrict
                                                    ,startProvinceCode: freightRouterInstance.startProvinceCode
                                                    ,startCityCode: freightRouterInstance.startCityCode
                                                    ,startDistrictCode: freightRouterInstance.startDistrictCode
                                                    ,endProvince: freightRouterInstance.endProvince
                                                    ,endCity: freightRouterInstance.endCity
                                                    ,endDistrict: freightRouterInstance.endDistrict
                                                    ,endProvinceCode: freightRouterInstance.endProvinceCode
                                                    ,endCityCode: freightRouterInstance.endCityCode
                                                    ,endDistrictCode: freightRouterInstance.endDistrictCode
                                                    ,provenance: freightRouterInstance.startProvince+'/'+freightRouterInstance.startCity+'/'+freightRouterInstance.startDistrict
                                                    ,destination: freightRouterInstance.endProvince+'/'+freightRouterInstance.endCity+'/'+freightRouterInstance.endDistrict
                                                    ,viaLand: freightRouterInstance.viaLand]])

        }
    }
    def update(){
        withFreightRouter(params.long('id')) { freightRouterInstance ->
            println '====request.JSON.freightRouter====' + request.JSON
            freightRouterInstance.properties = request.JSON
            freightRouterInstance.save(flush: true, failOnError: true)
            renderSuccess()

        }
    }

    private withFreightRouter(Long id, Closure c) {
        FreightRouter freightRouterInstance = id ? FreightRouter.get(id) : null
        if (freightRouterInstance) {
            c.call freightRouterInstance
        } else {
            renderNoTFoundError()
        }

    }
}
