package com.hfyz.workOrder

import com.hfyz.support.AlarmType
import com.hfyz.warning.AlarmLevel

class WorkOrder {
    String sn              //工单编号
    AlarmType alarmType    //报警类型
    AlarmLevel alarmLevel  //报警等级
    String companyCode     //业户编码
    String frameNo         //车架号
    String userID          //从业人员身份证号
    Date dateCreated
    Date checkTime          //检查时间
    Date rectificationTime  //整改时间
    String note             //备注
    WorkOrderStatus status=WorkOrderStatus.DSH    //工单状态

    static constraints = {
        sn nullable: false, blank: false, maxSize: 30, unique: true
        alarmType nullable: false
        alarmLevel nullable: false
        companyCode nullable: true, blank: false,maxSize: 30
        frameNo nullable: true, blank: false,maxSize: 20
        userID nullable: true,blank: false,maxSize: 18
        checkTime nullable: false
        rectificationTime nullable: false
        note nullable: true,blank: false,maxSize: 300
        status nullable: false
    }

    static  mapping = {
        id generator:'native', params:[sequence:'work_order_seq'], defaultValue: "nextval('work_order_seq')"
    }

}
