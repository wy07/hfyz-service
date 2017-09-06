package com.hfyz.infoCenter

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.rectification.HiddenRectificationOrderStatus
import com.hfyz.workOrder.WorkOrder
import com.hfyz.workOrder.WorkOrderStatus

class InBoxController implements ControllerHelper{

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def list = InBox.createCriteria().list([max:max, offset:offset, sort: 'isRead']){
            eq("accepter", getCurrentUser())
        }?.collect(){ InBox inBox ->
            def action
            if(inBox.sourceType == SourceType.GD){
                action = WorkOrderStatus.getInstanceById(inBox.action.toBigInteger())?.name()
            }
            if(inBox.sourceType == SourceType.YHZGD) {
                action = HiddenRectificationOrderStatus.getInstanceById(inBox.action.toBigInteger())?.name()
            }
            [id           : inBox.id
             , sourceId   : inBox.sourceId
             , sourceType : inBox.sourceType.cnName
             , title      : inBox.title
             , isRead     : inBox.isRead
             , dateCreated: inBox.dateCreated.format('yyyy-MM-dd HH:mm:ss ')
             , action     : action
            ]
        }
        def total = InBox.createCriteria().get {
            projections {
                count()
            }
            eq("accepter", getCurrentUser())
        }
        renderSuccessesWithMap([list: list, total: total])
    }

    def changeState() {
        withInBox(params.long('id')){InBox inBox ->
            inBox.isRead = true
            inBox.save(flush: true, failOnError: true)
        }
        renderSuccess()
    }

    def unreadMessage() {
        def list = InBox.createCriteria().list(){
            eq("accepter", getCurrentUser())
            and {
                eq("isRead", false)
            }
        }
        renderSuccessesWithMap([isShow: list.size()>0])
    }

    private withInBox(Long id,Closure c){
        InBox inBox = id ? InBox.get(id) : null
        if(inBox){
            c.call(inBox)
        }else{
            renderNoTFoundError()
        }
    }
}
