package com.hfyz.workOrder

enum WorkOrderStatus {
    DSH(1,'待审核')

    int id
    String cnName

    public WorkOrderStatus(int id, String cnName) {
        this.id=id
        this.cnName = cnName
    }

    public String toString() {
        cnName
    }
}