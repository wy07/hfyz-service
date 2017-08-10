package com.hfyz.workOrder

enum WorkOrderOperate {
    SP_T(1,'审批通过')
    ,SP_F(2,'审批拒绝')
    ,SP_AUTO(3,'自动审批')
    ,FK(4,'反馈')
    ,YP_T(5,'研判通过')
    ,YP_F(6,'研判拒绝')

    String cnName
    int id

    public WorkOrderOperate(int id,String cnName) {
        this.cnName=cnName
        this.id=id
    }

    public String toString() {
        cnName
    }
}
