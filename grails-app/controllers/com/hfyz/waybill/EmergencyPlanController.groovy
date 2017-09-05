package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.hfyz.support.SystemCode

class EmergencyPlanController implements ControllerHelper {

    def index() { }

    def getEmergencyPlanByDangerousType() {
        def dangerousType = SystemCode.get(request.JSON.id)
        def emergencyPlanList = EmergencyPlan.findAllByDangerousType(dangerousType).collect {
            [id:it.id, name: it.name, describe: it.describe]
        }
        renderSuccessesWithMap([emergencyPlanList: emergencyPlanList])
    }
}
