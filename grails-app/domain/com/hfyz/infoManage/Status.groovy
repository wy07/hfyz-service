package com.hfyz.infoManage

/**
 * Created by Administrator on 2017/7/5 0005.
 */
enum Status {
    QC("起草", 0) ,
    DSH("待审核",1) ,
    YTG("已通过",2) ,
    YJJ("已拒绝",3)

    String type
    int id
    Status(String type, int id){
        this.type = type;
        this.id = id
    }
}