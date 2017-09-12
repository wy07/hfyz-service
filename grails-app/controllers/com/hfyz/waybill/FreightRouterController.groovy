package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.security.User

class FreightRouterController implements ControllerHelper {

    def freightRouterService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(freightRouterService.getListAndTotal(max, offset, currentUser))
    }

    def save() {
        if (!currentUser.isCompanyUser()) {
            renderNoInstancePermError()
            return
        }
        FreightRouter freightRouterInstance = new FreightRouter(request.JSON)
        freightRouterInstance.companyCode = currentUser.companyCode
        freightRouterInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def delete() {
        withCompanyFreightRouter(params.long('id'), currentUser) { freightRouterInstance ->
            freightRouterInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def edit() {
        withCompanyFreightRouter(params.long('id'), currentUser) { freightRouterInstance ->
            renderSuccessesWithMap([freightRouter: freightRouterInstance as Map])
        }
    }

    def show() {
        withFreightRouter(params.long('id')) { freightRouterInstance ->
            if (currentUser.isCompanyUser() && freightRouterInstance.companyCode != currentUser.companyCode) {
                renderNoInstancePermError()
                return
            }
            renderSuccessesWithMap([freightRouter: freightRouterInstance as Map])
        }
    }

    def update() {
        withCompanyFreightRouter(params.long('id'), currentUser) { freightRouterInstance ->
            freightRouterInstance.properties = request.JSON
            freightRouterInstance.save(flush: true, failOnError: true)
            renderSuccess()

        }
    }

    def getViaLand() {
        def viaLands = FreightRouter.findAllByStartDistrictCodeAndEndDistrictCodeAndCompanyCode(request.JSON.startDistrictCode
                       ,request.JSON.endDistrictCode, request.JSON.companyCode).collect{obj ->
            [
                    id: obj.id,
                    viaLand: obj.viaLand,
                    routerName: obj.routerName
            ]
        }
        renderSuccessesWithMap([viaLands: viaLands])
    }

    private withFreightRouter(Long id, Closure c) {
        FreightRouter freightRouterInstance = id ? FreightRouter.get(id) : null
        if (freightRouterInstance) {
            c.call freightRouterInstance
        } else {
            renderNoTFoundError()
        }
    }

    private withCompanyFreightRouter(Long id, User user, Closure c) {
        FreightRouter freightRouterInstance = id ? FreightRouter.get(id) : null
        if (freightRouterInstance) {
            if (freightRouterInstance.companyCode != user.companyCode) {
                renderNoInstancePermError()
            } else {
                c.call freightRouterInstance
            }
        } else {
            renderNoTFoundError()
        }
    }
}
