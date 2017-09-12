package com.hfyz.support

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils


class ElectricFenceController implements ControllerHelper {

    def electricFenceService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = electricFenceService.search([name: request.JSON.name], max, offset)
        renderSuccessesWithMap(result)
    }

    def save() {
        ElectricFence electricFenceInstance = new ElectricFence(request.JSON)
        electricFenceInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withElectricFence(params.long('id')) { ElectricFence electricFenceInstance ->
            renderSuccessesWithMap([electricFence: [id           : electricFenceInstance.id
                                                    , name       : electricFenceInstance.name
                                                    , coordinates: electricFenceInstance.coordinates]])
        }
    }

    def update() {
        withElectricFence(params.long('id')) { ElectricFence electricFenceInstance ->
            electricFenceInstance.properties = request.JSON
            electricFenceInstance.save(flush: true, failOnError: true)
            renderSuccess()
        }
    }

    def delete() {
        withElectricFence(params.long('id')) { ElectricFence electricFenceInstance ->
            electricFenceInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def show() {
        withElectricFence(params.long('id')) { ElectricFence electricFenceInstance ->
            renderSuccessesWithMap([electricFence: [id           : electricFenceInstance.id
                                                    , name       : electricFenceInstance.name
                                                    , coordinates: electricFenceInstance.coordinates]])
        }
    }

    private withElectricFence(Long id, Closure c) {
        ElectricFence electricFenceInstance = id ? ElectricFence.get(id) : null
        if (electricFenceInstance) {
            c.call electricFenceInstance
        } else {
            renderNoTFoundError()
        }
    }
}
