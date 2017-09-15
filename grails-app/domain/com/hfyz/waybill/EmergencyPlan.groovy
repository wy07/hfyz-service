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

    static mapping = {
        comment '应急预案表'
        id generator: 'native', params: [sequence: 'emergency_plan_seq'], defaultValue: "nextval('emergency_plan_seq')"
        name comment:'应急预案名称'
        describe comment:'描述'
        dangerousType comment:'危险品运输类型'
    }
}
