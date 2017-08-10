package com.hfyz.workOrder

import com.commons.exception.IllegalActionException
import com.commons.exception.InstancePermException
import com.commons.exception.RecordNotFoundException
import com.hfyz.security.User

class WorkOrderService {
    def findWorkOrderListAndTotal(max, offset) {
        def workOrderList = WorkOrder.list([max: max, offset: offset, sort: 'id', order: 'desc'])?.collect { WorkOrder obj ->
            [id                 : obj.id
             , sn               : obj.sn
             , alarmType        : obj.alarmType.name
             , alarmLevel       : obj.alarmLevel.cnName
             , companyCode      : obj.companyCode
             , ownerName        : obj.ownerName
             , operateManager   : obj.operateManager
             , phone            : obj.phone
             , frameNo          : obj.frameNo
             , userID           : obj.userID
             , dateCreated      : obj.dateCreated.format('yyyy-MM-dd HH:mm')
             , checkTime        : obj.checkTime.format('yyyy-MM-dd HH:mm')
             , rectificationTime: obj.rectificationTime.format('yyyy-MM-dd HH:mm')
             , note             : obj.note
             , status           : obj.status.cnName]
        }
        return [workOrderList: workOrderList, total: WorkOrder.count()]
    }


    def findPendingWorkOrderListAndTotal(max, offset, roles) {


        def workOrderList = WorkOrder.findAllByStatusInListAndTodoRoleInList([WorkOrderStatus.DSH, WorkOrderStatus.DYP], roles, [max: max, offset: offset, sort: 'lastUpdated', order: 'desc'])?.collect { WorkOrder obj ->
            [id                 : obj.id
             , sn               : obj.sn
             , alarmType        : obj.alarmType.name
             , alarmLevel       : obj.alarmLevel.cnName
             , companyCode      : obj.companyCode
             , ownerName        : obj.ownerName
             , operateManager   : obj.operateManager
             , phone            : obj.phone
             , frameNo          : obj.frameNo
             , userID           : obj.userID
             , dateCreated      : obj.dateCreated.format('yyyy-MM-dd HH:mm')
             , checkTime        : obj.checkTime.format('yyyy-MM-dd HH:mm')
             , rectificationTime: obj.rectificationTime.format('yyyy-MM-dd HH:mm')
             , note             : obj.note
             , status           : [value: obj.status.name(), name: obj.status.cnName]]
        }

        def workOrderCount = WorkOrder.countByStatusInListAndTodoRoleInList([WorkOrderStatus.DSH, WorkOrderStatus.DYP], roles)

        return [workOrderList: workOrderList, total: workOrderCount]
    }

    def findFeedbackWorkOrderListAndTotal(max, offset, User user) {
        def roles = user.authorities?.authority
        def workOrderList = WorkOrder.findAllByCompanyCodeAndStatusAndTodoRoleInList(user.companyCode, WorkOrderStatus.DFK, roles, [max: max, offset: offset, sort: 'lastUpdated', order: 'desc'])?.collect { WorkOrder obj ->
            [id                 : obj.id
             , sn               : obj.sn
             , alarmType        : obj.alarmType.name
             , alarmLevel       : obj.alarmLevel.cnName
             , companyCode      : obj.companyCode
             , ownerName        : obj.ownerName
             , operateManager   : obj.operateManager
             , phone            : obj.phone
             , frameNo          : obj.frameNo
             , userID           : obj.userID
             , dateCreated      : obj.dateCreated.format('yyyy-MM-dd HH:mm')
             , checkTime        : obj.checkTime.format('yyyy-MM-dd HH:mm')
             , rectificationTime: obj.rectificationTime.format('yyyy-MM-dd HH:mm')
             , note             : obj.note
             , status           : [value: obj.status.name(), name: obj.status.cnName]]
        }
        def workOrderCount = WorkOrder.countByCompanyCodeAndStatusAndTodoRoleInList(user.companyCode, WorkOrderStatus.DFK, roles)
        return [workOrderList: workOrderList, total: workOrderCount]
    }

    WorkOrder getWorkOrderById(Long id) {
        WorkOrder workOrderInstance = id ? WorkOrder.get(id) : null
        if (!workOrderInstance) {
            throw new RecordNotFoundException()
        }
        return workOrderInstance
    }

    def preExamine(Long id, User user) {
        WorkOrder workOrder = getWorkOrderById(id)

        if (workOrder.status != WorkOrderStatus.DSH) {
            throw new IllegalActionException()
        }

        if (!(workOrder.todoRole in user.authorities?.authority)) {
            throw new InstancePermException()
        }

        [workOrder         : workOrder as Map
         , workOrderRecords: workOrder.workOrderRecords?.collect { WorkOrderRecord record ->
            record as Map
        }]
    }

    def examine(Long id, User user, String note, boolean result) {
        WorkOrder workOrder = getWorkOrderById(id)

        if (workOrder.status != WorkOrderStatus.DSH) {
            throw new IllegalActionException()
        }

        if (!(workOrder.todoRole in user.authorities?.authority)) {
            throw new InstancePermException()
        }

        if (result) {
            approvalExamine(workOrder, user, note)
        } else {
            refuseExamine(workOrder, user, note)
        }


    }

    def preFeedback(Long id, User user) {
        WorkOrder workOrder = getWorkOrderById(id)

        if (workOrder.companyCode != user.companyCode) {
            throw new InstancePermException()
        }

        if (!(workOrder.todoRole in user.authorities?.authority)) {
            throw new InstancePermException()
        }

        if (workOrder.status != WorkOrderStatus.DFK) {
            throw new IllegalActionException()
        }

        [workOrder: workOrder as Map]
    }

    def feedback(Long id, User user, String note) {
        WorkOrder workOrder = getWorkOrderById(id)

        if (workOrder.companyCode != user.companyCode) {
            throw new InstancePermException()
        }

        if (!(workOrder.todoRole in user.authorities?.authority)) {
            throw new InstancePermException()
        }

        if (workOrder.status != WorkOrderStatus.DFK) {
            throw new IllegalActionException()
        }

        workOrder.addToWorkOrderRecords(new WorkOrderRecord(user: user
                , note: note
                , workOrderStatus: workOrder.status
                , operate: WorkOrderOperate.FK))

        def flow = workOrder.flows[workOrder.flowStep]

        //添加工单过期的判断，若过期 生成二次工单


        workOrder.todoRole = flow.role
        workOrder.flowStep += 1
        workOrder.status = WorkOrderFlowAction.valueOf(flow.action).workOrderStatus

        workOrder.save(flush: true, failOnError: true)
    }

    def preJudge(Long id, User user) {
        WorkOrder workOrder = getWorkOrderById(id)

        if (workOrder.status != WorkOrderStatus.DYP) {
            throw new IllegalActionException()
        }

        if (!(workOrder.todoRole in user.authorities?.authority)) {
            throw new InstancePermException()
        }

        [workOrder         : workOrder as Map
         , workOrderRecords: workOrder.workOrderRecords?.collect { WorkOrderRecord record ->
            record as Map
        }]
    }

    def judge(Long id, User user, String note, boolean result) {
        WorkOrder workOrder = getWorkOrderById(id)

        if (workOrder.status != WorkOrderStatus.DYP) {
            throw new IllegalActionException()
        }

        if (!(workOrder.todoRole in user.authorities?.authority)) {
            throw new InstancePermException()
        }

        if (result) {
            approvalJudge(workOrder, user, note)
        } else {
            refuseJudge(workOrder, user, note)
        }


    }

    private static approvalJudge(WorkOrder workOrder, User user, String note) {
        workOrder.addToWorkOrderRecords(new WorkOrderRecord(user: user
                , note: note
                , workOrderStatus: workOrder.status
                , operate: WorkOrderOperate.YP_T))
        workOrder.status = WorkOrderStatus.YWC
        workOrder.flowStep = null
        workOrder.todoRole = null
        workOrder.passed = true

        workOrder.save(flush: true, failOnError: true)

    }

    private static refuseJudge(WorkOrder workOrder, User user, String note) {
        workOrder.addToWorkOrderRecords(new WorkOrderRecord(user: user
                , note: note
                , workOrderStatus: workOrder.status
                , operate: WorkOrderOperate.YP_F))

        workOrder.status = WorkOrderStatus.YWC
        workOrder.flowStep = null
        workOrder.todoRole = null
        workOrder.passed = false
        workOrder.save(flush: true, failOnError: true)

        if (workOrder.parent) {
            return
        }

        WorkOrder newWorkerOrder = new WorkOrder(sn: System.currentTimeMillis() + "" + new Random().nextInt(100000).toString().padLeft(5, '0')
                , alarmType: workOrder.alarmType
                , alarmLevel: workOrder.alarmLevel
                , companyCode: workOrder.companyCode
                , ownerName: workOrder.ownerName
                , operateManager: workOrder.operateManager
                , phone: workOrder.phone
                , frameNo: workOrder.frameNo
                , userID: workOrder.userID
                , checkTime: workOrder.checkTime
                , rectificationTime: new Date() + 2
                , note: workOrder.note
                , parent: workOrder
                , flows: [])

        def flows = WorkOrderFlow.findByAlarmTypeAndEnabled(workOrder.alarmType, true)?.flows
        int startStep = flows.findIndexOf {
            it.action != WorkOrderFlowAction.SP.name()
        }
        (startStep > -1 ? flows[startStep..-1] : flows).each {
            newWorkerOrder.flows << it
        }

        if (startStep > -1) {
            newWorkerOrder.addToWorkOrderRecords(new WorkOrderRecord(note: "研判未通过，系统自动二次派单。"
                    , operate: WorkOrderOperate.SP_AUTO))
        }

        def flow = newWorkerOrder.flows[0]
        newWorkerOrder.todoRole = flow.role
        newWorkerOrder.flowStep = 1
        newWorkerOrder.status = WorkOrderFlowAction.valueOf(flow.action).workOrderStatus

        newWorkerOrder.save(flush: true, failOnError: true)

    }

    private static approvalExamine(WorkOrder workOrder, User user, String note) {

        workOrder.addToWorkOrderRecords(new WorkOrderRecord(user: user
                , note: note
                , workOrderStatus: workOrder.status
                , operate: WorkOrderOperate.SP_T))

        def flow = workOrder.flows[workOrder.flowStep]

        workOrder.todoRole = flow.role
        workOrder.flowStep += 1
        workOrder.status = WorkOrderFlowAction.valueOf(flow.action).workOrderStatus

        workOrder.save(flush: true, failOnError: true)


    }

    private static refuseExamine(WorkOrder workOrder, User user, String note) {
        workOrder.addToWorkOrderRecords(new WorkOrderRecord(user: user
                , note: note
                , workOrderStatus: workOrder.status
                , operate: WorkOrderOperate.SP_F))

        if (workOrder.flowStep == 1) {
            workOrder.status = WorkOrderStatus.YQX
            workOrder.flowStep = null
            workOrder.todoRole = null
        } else {
            def flow = workOrder.flows[workOrder.flowStep - 2]

            workOrder.todoRole = flow.role
            workOrder.flowStep -= 1
            workOrder.status = WorkOrderFlowAction.valueOf(flow.action).workOrderStatus
        }
        workOrder.save(flush: true, failOnError: true)
    }

}
