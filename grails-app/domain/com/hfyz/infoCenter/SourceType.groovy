package com.hfyz.infoCenter

enum SourceType {
      GD(1,'工单')
    , YHZGD(2,'隐患整改单')
    , DZLD(3,'电子路单')

    int id
    String cnName

    SourceType(int id,String cnName) {
        this.id=id
        this.cnName = cnName
    }
}
