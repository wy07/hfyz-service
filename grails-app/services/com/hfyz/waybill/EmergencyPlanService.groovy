package com.hfyz.waybill

import grails.transaction.Transactional

@Transactional
class EmergencyPlanService {

    def getEmergencyPlanListAndTotal(def max, def offset) {
        def emergencyPlanList = EmergencyPlan.list([max: max, offset: offset, sort: 'id'])?.collect { EmergencyPlan emergencyPlan->
            [id       :emergencyPlan.id
             ,name    :emergencyPlan.name
             ,describe   :emergencyPlan.describe
             ,dangerousType:emergencyPlan.dangerousType.name]
        }

        def total = EmergencyPlan.count()
        return [emergencyPlanList: emergencyPlanList, total: total]
    }
}
