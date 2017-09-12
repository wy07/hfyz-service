package com.hfyz.workOrder

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.security.Role

class WorkOrderFlowController implements ControllerHelper {
    def workOrderService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(workOrderService.findWorkOrderFlowListAndCount(max, offset))
    }

    def effect() {
        withFlow(params.long('id')) { workOrderFlow ->
            workOrderService.makeFlowEffective(workOrderFlow)
            renderSuccess()
        }
    }

    def save() {
        workOrderService.saveFlow(request.JSON)
        renderSuccess()
    }

    def edit() {
        withFlow(params.long('id')) { WorkOrderFlow workOrderFlow ->
            renderSuccessesWithMap([workOrderFlow: [id           : workOrderFlow.id
                                                    , alarmType  : workOrderFlow.alarmType.id
                                                    , flowVersion: workOrderFlow.flowVersion
                                                    , flows      : workOrderFlow.flows
                                                    , enabled    : workOrderFlow.enabled
                                                    , dateCreated: workOrderFlow.dateCreated.format("yyyy-MM-dd")
                                                    , lastUpdated: workOrderFlow.lastUpdated.format("yyyy-MM-dd")]])
        }
    }

    def show() {
        withFlow(params.long('id')) { WorkOrderFlow workOrderFlow ->

            def flows=workOrderFlow.flows.collect{flow->
                Role role=Role.findByAuthority(flow.role)
                [name:flow.name
                 ,action:WorkOrderFlowAction.valueOf(flow.action).cnName
                 ,role:role.name
                 ,org:role.org.name]
            }

            renderSuccessesWithMap([workOrderFlow: [id           : workOrderFlow.id
                                                    , alarmType  : workOrderFlow.alarmType.name
                                                    , flowVersion: workOrderFlow.flowVersion
                                                    , flows      : flows
                                                    , enabled    : workOrderFlow.enabled
                                                    , dateCreated: workOrderFlow.dateCreated.format("yyyy-MM-dd")
                                                    , lastUpdated: workOrderFlow.lastUpdated.format("yyyy-MM-dd")]])
        }
    }

    def update(){
        withFlow(params.long('id')) { WorkOrderFlow workOrderFlow ->
            workOrderService.updateFlow(workOrderFlow,request.JSON)
            renderSuccess()
        }
    }

    private withFlow(Long id, Closure c) {
        WorkOrderFlow flow = id ? WorkOrderFlow.get(id) : null
        if (flow) {
            c.call(flow)
        } else {
            renderNoTFoundError()
        }
    }
}
