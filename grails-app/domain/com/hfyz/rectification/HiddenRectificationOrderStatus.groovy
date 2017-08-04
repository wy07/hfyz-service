package com.hfyz.rectification


enum HiddenRectificationOrderStatus {
    QC("起草", 0) ,
    DSH("待审核",1) ,
    DFK("待反馈",2) ,
    YJJ("已拒绝",3),
    DYR('待确认',4),
    HG('合格',5),
    BHG('不合格',6)

    String type
    int id

    HiddenRectificationOrderStatus(String type, int id){
        this.type = type;
        this.id = id
    }
}