package com.hfyz.workOrder

import com.hfyz.support.AlarmType
import com.hfyz.warning.AlarmLevel

class WorkOrderService {
    def list(){
        def workOrderList = WorkOrder.list([max:request.JSON.max, offset:request.JSON.offset, sort: "id"])?.collect { WorkOrder obj ->
            [ id: obj.id
              , sn: obj.sn
              , alarmType: obj.alarmType
              , alarmLevel: obj.alarmLevel
              , companyCode: obj.companyCode
              , ownerName: obj.ownerName
              , operateManager: obj.operateManager
              , phone: obj.phone
              , frameNo: obj.frameNo
              , userID: obj.userID
              , dateCreated: obj.dateCreated
              , checkTime: obj.checkTime
              , rectificationTime: obj.rectificationTime
              , note: obj.note
              , status: obj.status]
        }
        renderSuccessesWithMap([workOrderList: workOrderList, total: WorkOrder.count()])
    }
//    String sn              //工单编号
//    AlarmType alarmType    //报警类型
//    AlarmLevel alarmLevel  //报警等级
//    String companyCode     //业户编码
//    String ownerName       //业户名称
//    String operateManager  //经营负责人
//    String phone           //电话
//    String frameNo         //车架号
//    String userID          //从业人员身份证号
//    Date dateCreated
//    Date checkTime          //检查时间
//    Date rectificationTime  //整改时间
//    String note             //备注
//    WorkOrderStatus status=WorkOrderStatus.DSH
}
