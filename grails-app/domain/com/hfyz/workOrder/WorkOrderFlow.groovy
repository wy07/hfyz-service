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
        comment '工单流信息表'
        id generator:'native', params:[sequence:'work_order_flow_seq'], defaultValue: "nextval('work_order_flow_seq')"
        alarmType comment:'告警类型'
        flowVersion comment:'工作流版本'
        flows type: JsonbListType, sqlType: 'jsonb', comment:'元素流'
        enabled comment:'是否生效'
        dateCreated comment:'创建时间'
        lastUpdated comment:'最后更新时间'
    }
}
