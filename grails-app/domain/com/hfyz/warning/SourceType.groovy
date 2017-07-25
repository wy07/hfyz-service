package com.hfyz.warning
enum SourceType {
    CAR(1,'车辆')
    , USER(2,'用户')
    , COMPANY(3,'企业')

    int id
    String cnName

    public SourceType(int id,String cnName) {
        this.id=id
        this.cnName = cnName
    }

    public String toString() {
        cnName
    }
}