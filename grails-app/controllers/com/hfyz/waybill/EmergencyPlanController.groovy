package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.support.DangerousType
import com.hfyz.support.SystemCode

class EmergencyPlanController implements ControllerHelper {

    def emergencyPlanService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(emergencyPlanService.getEmergencyPlanListAndTotal(max,offset))
    }

    def getEmergencyPlanByDangerousType() {
        def dangerousType = SystemCode.get(request.JSON.id)
        def emergencyPlanList = EmergencyPlan.findAllByDangerousType(dangerousType).collect {
            [id: it.id, name: it.name, describe: it.describe]
        }
        renderSuccessesWithMap([emergencyPlanList: emergencyPlanList])
    }

    def save() {
        EmergencyPlan emergencyPlan = new EmergencyPlan(request.JSON)
        emergencyPlan.dangerousType = DangerousType.get(request.JSON.dangerousTypeId)
        emergencyPlan.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withEmergencyPlan(params.long('id')) { emergencyPlanInstance ->
            renderSuccessesWithMap([emergencyPlan: [id: emergencyPlanInstance.id
                                                   ,name: emergencyPlanInstance.name
                                                   ,describe: emergencyPlanInstance.describe
                                                   ,dangerousTypeId: emergencyPlanInstance.dangerousType.id
                                                   ,dangerousTypeName: emergencyPlanInstance.dangerousType.name]])
        }
    }

    def update() {
        withEmergencyPlan(params.long('id')) { emergencyPlanInstance ->
            emergencyPlanInstance.properties = request.JSON
            emergencyPlanInstance.dangerousType = DangerousType.get(request.JSON.dangerousTypeId)
            emergencyPlanInstance.save(flush: true, failOnError: true)
            renderSuccess()

        }
    }

    def delete() {
        withEmergencyPlan(params.long('id')) { emergencyPlanInstance ->
            if(FreightWaybill.findByEmergencyPlan(emergencyPlanInstance)) {
                renderErrorMsg('此应急预案已被引用，不能删除！')
                return
            }
            emergencyPlanInstance.delete(flush: true)
            renderSuccess()
        }
    }

    private withEmergencyPlan(Long id, Closure c) {
        EmergencyPlan emergencyPlanInstance = id ? EmergencyPlan.get(id) : null
        if (emergencyPlanInstance) {
            c.call emergencyPlanInstance
        } else {
            renderNoTFoundError()
        }
    }
}
