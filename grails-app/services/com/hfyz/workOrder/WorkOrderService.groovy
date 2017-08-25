package com.hfyz.workOrder

import com.commons.exception.IllegalActionException
import com.commons.exception.InstancePermException
import com.commons.exception.RecordNotFoundException
import com.commons.utils.NumberUtils
import com.commons.utils.SQLHelper
import com.hfyz.owner.OwnerIdentity
import com.hfyz.security.User
import com.hfyz.support.AlarmType
import grails.async.Promise

import static grails.async.Promises.task

class WorkOrderService {
    def dataSource
    def supportService


    def findWorkOrderListAndTotal(max, offset, User user) {
        def workOrderList = WorkOrder.createCriteria().list([max: max, offset: offset, sort: 'lastUpdated', order: 'desc']) {
            if (user.isCompanyUser()) {
                eq('companyCode', user.companyCode)
                ne('status', WorkOrderStatus.DSH)
                ne('status', WorkOrderStatus.YQX)
            }
        }?.collect { WorkOrder obj ->
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

        def workOrderCount = WorkOrder.createCriteria().get {
            projections {
                count()
            }
            if (user.isCompanyUser()) {
                eq('companyCode', user.companyCode)
                ne('status', WorkOrderStatus.DSH)
                ne('status', WorkOrderStatus.YQX)
            }
        }
        return [workOrderList: workOrderList, total: workOrderCount]
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

    def statistic(Map inputParams, User user, Long max, Long offset) {
        def sqlParams = [:]

        if (user.isCompanyUser()) {
            sqlParams.companyCode = user.companyCode
        } else if (inputParams.companyName) {
            sqlParams.companyName = "${inputParams.companyName}%".toString()
        }
        if (inputParams.alarmTypeId) {
            sqlParams.alarmTypeId = inputParams.alarmTypeId

        }
        if (inputParams.startDate) {
            sqlParams.startDate = inputParams.startDate.format("yyyy-MM-dd")
        }
        if (inputParams.endDate) {
            sqlParams.endDate = inputParams.endDate.format("yyyy-MM-dd")
        }

        def statisticList = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(getStatisticSql(sqlParams.companyName, sqlParams.companyCode, sqlParams.alarmTypeId, sqlParams.stateTime, sqlParams.endDate), sqlParams + [max: user.isCompanyUser() ? 1 : max, offset: user.isCompanyUser() ? 0 : offset])
        }?.collect { obj ->
            [companyCode     : obj.company_code
             , allOrder      : obj.all_order
             , doneOrder     : obj.done_order
             , doingOrder    : obj.doing_order
             , completionRate: obj.completion_rate
             , alarmType     : obj.alarm_type_name
             , companyName   : obj.company_name]
        }

        def statisticCount = user.isCompanyUser() ? 1 : OwnerIdentity.count()


        [statisticList: statisticList, statisticCount: statisticCount]
    }


    def findWorkOrderFlowListAndCount(max, offset) {
        def list = WorkOrderFlow.createCriteria().list(max: max, offset: offset) {
            order("enabled", "desc")
            order("alarmType", "desc")
            order("flowVersion", "desc")
        }?.collect { WorkOrderFlow flow ->
            [id           : flow.id
             , alarmType  : flow.alarmType.name
             , flowVersion: flow.flowVersion
             , enabled    : flow.enabled
             , dateCreated: flow.dateCreated.format("yyyy-MM-dd")
             , lastUpdated: flow.lastUpdated.format("yyyy-MM-dd")]
        }

        def count = WorkOrderFlow.count()
        [workOrderFlowList: list, workOrderFlowCount: count]
    }

    def saveFlow(def params) {
        AlarmType alarmType = supportService.getAlarmType(NumberUtils.toLong(params.alarmType))
        int lastVersion = WorkOrderFlow.findByAlarmType(alarmType, [sort: 'dateCreated', order: 'desc'])?.flowVersion ?: 0
        WorkOrderFlow flow = new WorkOrderFlow(alarmType: alarmType
                , flowVersion: lastVersion + 1
                , flows: [])
        params.examineFlows.each { obj ->
            flow.flows << [name: obj.name, role: obj.role, action: WorkOrderFlowAction.SP.name()]
        }
        flow.flows << [name: params.feedbackFlow.name, role: params.feedbackFlow.role, action: WorkOrderFlowAction.FK.name()]
        flow.flows << [name: params.judgeFlow.name, role: params.judgeFlow.role, action: WorkOrderFlowAction.YP.name()]
        flow.save(flush: true, failOnError: true)
    }

    def updateFlow(WorkOrderFlow flow, def params) {
        flow.flows = []
        params.examineFlows.each { obj ->
            flow.flows << [name: obj.name, role: obj.role, action: WorkOrderFlowAction.SP.name()]
        }
        flow.flows << [name: params.feedbackFlow.name, role: params.feedbackFlow.role, action: WorkOrderFlowAction.FK.name()]
        flow.flows << [name: params.judgeFlow.name, role: params.judgeFlow.role, action: WorkOrderFlowAction.YP.name()]
        flow.alarmType = supportService.getAlarmType(NumberUtils.toLong(params.alarmType))
        flow.save(flush: true, failOnError: true)
    }

    def makeFlowEffective(WorkOrderFlow workOrderFlow) {
        if (workOrderFlow.enabled) {
            throw new IllegalActionException('该工作流已生效，请勿重复操作！')
        }
        WorkOrderFlow.executeUpdate("update WorkOrderFlow flow set flow.enabled=false where flow.alarmType=:alarmType", [alarmType: workOrderFlow.alarmType])
        workOrderFlow.enabled = true
        workOrderFlow.save(flush: true, failOnError: true)
    }

    private String getStatisticSql(String companyName, String companyCode, Long alarmTypeId, String startDate, String endDate) {
        def companys = {
            String sql = """
                select company.id
                    ,company.owner_name company_name
                    ,company.company_code
                from owner_basicinfo_owneridentity company
                where 1=1
            """

            if (companyCode) {
                sql += " and company.company_code=:companyCode"
            } else if (companyName) {
                sql += " and company.owner_name like :companyName"
            }

            sql += """
                order by company.id desc
                limit :max offset :offset
            """
            return sql
        }

        def orders = {
            String sql = """
                select company.company_name
                    ,company.company_code
                    ,work_order.alarm_type_id
                    ,work_order.id work_order_id
                    ,work_order.status
                from companys company
                left join work_order on company.company_code=work_order.company_code
                where ((work_order.status!=1 and work_order.status!=5) or work_order.status is null)
            """

            if (startDate) {
                sql += " and work_order.check_time>=:startDate::timestamp"
            }
            if (endDate) {
                sql += " and work_order.check_time < :endDate::timestamp + '1 day'"
            }
            return sql

        }

        def orderAlarms = {
            String sql = """
                select company.company_name
                    ,company.company_code
                    ,alarm_type.id alarm_type_id
                    ,work_order.work_order_id
                    ,case when work_order.status is null then 0 else 1 end as all_order
                    ,case when work_order.status=4 then 1 else 0 end as done_order
                from companys company
            """
            if (alarmTypeId) {
                sql += " left join system_code alarm_type on alarm_type.id=:alarmTypeId and alarm_type.type='ALARM_TYPE'"
            } else {
                sql += " left join system_code alarm_type on alarm_type.type='ALARM_TYPE'"
            }
            sql += " left join orders work_order on company.company_code=work_order.company_code and alarm_type.id=work_order.alarm_type_id"
            return sql
        }

        def sqlStr = """
            with companys as(
                ${companys()}
            ),orders as (
                ${orders()}
            ),orderAlarms as (
                ${orderAlarms()}
            ), statistic as(
                select company_code, alarm_type_id, sum(all_order) all_order,sum(done_order) done_order
                from orderAlarms
                group by company_code,alarm_type_id
            )
            select statistic.company_code
                ,statistic.all_order
                ,statistic.done_order
                ,statistic.all_order-statistic.done_order doing_order
                ,case when all_order=0 then 100
                when done_order=0 then 0
                else round(done_order*1.0/all_order*100,2)  end completion_rate
                ,alarm_type.name alarm_type_name
                ,company.company_name
            from statistic
            join system_code alarm_type on alarm_type.id=statistic.alarm_type_id
            join companys company on company.company_code=statistic.company_code
            order by company.id desc ,alarm_type_id desc
        """

        return sqlStr
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
