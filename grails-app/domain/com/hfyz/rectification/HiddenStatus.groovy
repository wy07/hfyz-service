package com.hfyz.rectification


enum HiddenStatus {
    QC("起草", 0) ,
    DSH("待审核",1) ,
    YTG("已通过",2) ,
    YJJ("已拒绝",3)

    String type
    int id

    HiddenStatus(String type, int id){
        this.type = type;
        this.id = id
    }
}