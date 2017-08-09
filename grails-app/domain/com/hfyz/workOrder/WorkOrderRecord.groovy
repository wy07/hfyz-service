package com.hfyz.workOrder

import com.hfyz.security.User

class WorkOrderRecord {
    WorkOrder workOrder
    User user
    String note
    WorkOrderStatus    workOrderStatus       //工单的状态
    Date dateCreated
    WorkOrderOperate operate

    static constraints = {
        workOrder nullable: false
        user nullable: false
        note nullable: false, blank: false, maxSize: 600
        operate nullable: false
        workOrderStatus nullable: false
    }
}
