package com.hfyz.workOrder

import com.hfyz.security.User

class WorkOrderRecord {
    WorkOrder workOrder
    User user
    String note
    WorkOrderStatus workOrderStatus       //工单的状态
    Date dateCreated
    WorkOrderOperate operate

    static constraints = {
        workOrder nullable: false
        user nullable: true
        note nullable: false, blank: false, maxSize: 600
        operate nullable: false
        workOrderStatus nullable: true
    }

    static mapping = {
        comment '工单记录表'
        id generator:'native', params:[sequence:'work_order_record_seq'], defaultValue: "nextval('work_order_record_seq')"
        workOrder comment:'工单源'
        user comment:'操作人'
        note comment:'备注'
        workOrderStatus comment:'工单状态'
        dateCreated comment:'创建时间'
        operate comment:'操作结果'
    }


    Object asType(Class clazz) {

        if (clazz == Map.class) {
            Map map = [
                    user             : this.user?.name
                    , note           : this.note
                    , workOrderStatus: this.workOrderStatus?.cnName
                    , dateCreated    : this.dateCreated.format('yyyy-MM-dd HH:mm')
                    , operate        : this.operate.cnName]

            return map
        }
        return null
    }
}
