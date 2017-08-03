package com.hfyz.rectification


enum HiddenRectificationOrderStatus {
    QC("起草", 0) ,
    DSH("待审核",1) ,
    YTG("已通过",2) ,
    YJJ("已拒绝",3)

    String type
    int id

    HiddenRectificationOrderStatus(String type, int id){
        this.type = type;
        this.id = id
    }
}