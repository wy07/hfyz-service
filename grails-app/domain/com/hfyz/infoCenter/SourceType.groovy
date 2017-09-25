package com.hfyz.infoCenter

enum SourceType {
      GD(1,'工单')
    , YHZGD(2,'隐患整改单')
    , DZLD(3,'电子路单')
    , BJ(4,'告警')
    , YH(5,'业户')
    , CL(6,'车辆')


    int id
    String cnName

    SourceType(int id,String cnName) {
        this.id=id
        this.cnName = cnName
    }
}
