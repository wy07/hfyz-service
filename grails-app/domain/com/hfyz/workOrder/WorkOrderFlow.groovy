package com.hfyz.workOrder

import com.hfyz.support.AlarmType
import com.hibernate.usertype.JsonbListType

class WorkOrderFlow {
    AlarmType alarmType     //告警类型
    Integer flowVersion     //工作流版本
    List flows               //工作流 元素：[role:'ROLE_1',name:'初步审批',action:WorkOrderFlowAction]
    boolean enabled=false   //是否生效
    Date dateCreated
    Date lastUpdated

    static constraints = {
        alarmType nullable: false
        flowVersion nullable: false,min: 1,unique: 'alarmType'
        flows nullable: false
    }

    static mapping = {
        flows type: JsonbListType, sqlType: 'jsonb'
    }
}
