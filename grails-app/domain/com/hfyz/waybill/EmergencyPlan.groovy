package com.hfyz.waybill

import com.hfyz.support.DangerousType

class EmergencyPlan {
    String name
    String describe
    DangerousType dangerousType

    static constraints = {
        name unique: true, nullable: false, blank: false
        describe nullable: false, blank: false, maxSize: 200
    }
}
