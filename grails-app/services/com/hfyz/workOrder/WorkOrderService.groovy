package com.hfyz.workOrder

class WorkOrderService {
    def list(max, offset){
        def workOrderList = WorkOrder.list([max: max, offset: offset, sort: 'id'])?.collect { WorkOrder obj ->
            [ id: obj.id
              , sn: obj.sn
              , alarmType: obj.alarmType.name
              , alarmLevel: obj.alarmLevel.cnName
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
              , status: obj.status.cnName]
        }
        return [workOrderList: workOrderList, total: WorkOrder.count()]
    }
}
