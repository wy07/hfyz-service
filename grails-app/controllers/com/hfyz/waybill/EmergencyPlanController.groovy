package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
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
            [id:it.id, name: it.name, describe: it.describe]
        }
        renderSuccessesWithMap([emergencyPlanList: emergencyPlanList])
    }
}
