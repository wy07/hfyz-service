package com.hfyz.workOrder

enum WorkOrderOperate {
    SP_T(1,'审批通过')
    ,SP_F(2,'审批拒绝')
    ,FK(3,'反馈')
    ,YP_T(4,'研判通过')
    ,YP_F(5,'研判拒绝')

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
