package com.hfyz.workOrder

enum WorkOrderFlowAction {
    SP('审批',WorkOrderStatus.DSH)
    ,FK('反馈',WorkOrderStatus.DFK)
    ,YP('研判',WorkOrderStatus.DYP)

    String cnName
    WorkOrderStatus workOrderStatus


    public WorkOrderFlowAction(String cnName,WorkOrderStatus workOrderStatus) {
        this.cnName=cnName
        this.workOrderStatus = workOrderStatus
    }

    public String toString() {
        cnName
    }
}