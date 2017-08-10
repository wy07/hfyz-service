package com.hfyz.workOrder

enum WorkOrderStatus {
    DSH(1,'待审核')
    ,DFK(2,'待反馈')
    ,DYP(3,'待研判')
    ,YWC(4,'已完成')
    ,YQX(5,'已取消')

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