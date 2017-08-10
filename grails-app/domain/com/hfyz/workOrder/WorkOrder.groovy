package com.hfyz.workOrder

import com.hfyz.support.AlarmType
import com.hfyz.warning.AlarmLevel
import com.hibernate.usertype.JsonbListType

class WorkOrder {
    String sn              //工单编号
    AlarmType alarmType    //报警类型
    AlarmLevel alarmLevel  //报警等级
    String companyCode     //业户编码
    String ownerName       //业户名称
    String operateManager  //经营负责人
    String phone           //电话
    String frameNo         //车架号
    String userID          //从业人员身份证号

    List flows             //工作流
    Integer flowStep       //工作流执行步骤
    String todoRole        //需执行角色名称


    Date dateCreated
    Date lastUpdated
    Date checkTime          //检查时间
    Date rectificationTime  //整改时间
    String note             //备注
    WorkOrderStatus status = WorkOrderStatus.DSH    //工单状态
    Boolean passed

    WorkOrder parent

    static hasMany = [workOrderRecords: WorkOrderRecord]

    static constraints = {
        sn nullable: false, blank: false, maxSize: 30, unique: true
        alarmType nullable: false
        alarmLevel nullable: false
        companyCode nullable: true, blank: false, maxSize: 30
        ownerName nullable: true, blank: false, maxSize: 30
        operateManager nullable: true, blank: false, maxSize: 30
        phone nullable: true, blank: false, maxSize: 30
        frameNo nullable: true, blank: false, maxSize: 20
        userID nullable: true, blank: false, maxSize: 18
        checkTime nullable: false
        rectificationTime nullable: false
        note nullable: true, blank: false, maxSize: 600
        status nullable: false
        flows nullable: false
        flowStep nullable: true, min: 1
        todoRole nullable: true
        parent nullable: true
        passed nullable: true
    }
    static mapping = {
        id generator: 'native', params: [sequence: 'work_order_seq'], defaultValue: "nextval('work_order_seq')"
        flows type: JsonbListType, sqlType: 'jsonb'
        workOrderRecords sort: 'dateCreated', order: 'asc'

    }


    Object asType(Class clazz) {

        if (clazz == Map.class) {
            Map map = [
                    id                 : this.id
                    , sn               : this.sn
                    , alarmType        : this.alarmType.name
                    , alarmLevel       : this.alarmLevel.cnName
                    , companyCode      : this.companyCode
                    , ownerName        : this.ownerName
                    , operateManager   : this.operateManager
                    , phone            : this.phone
                    , frameNo          : this.frameNo
                    , userID           : this.userID
                    , dateCreated      : this.dateCreated.format('yyyy-MM-dd HH:mm')
                    , checkTime        : this.checkTime.format('yyyy-MM-dd HH:mm')
                    , rectificationTime: this.rectificationTime.format('yyyy-MM-dd HH:mm')
                    , note             : this.note
                    , status           : this.status.cnName
                    , parent           : this.parent ? [id: this.parent.id, sn: this.parent.sn] : null
                    , passed           : this.passed
            ]
            return map
        }
        return null
    }
}
