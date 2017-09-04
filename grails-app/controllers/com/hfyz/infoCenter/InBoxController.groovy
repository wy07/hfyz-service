package com.hfyz.infoCenter

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.workOrder.WorkOrder

class InBoxController implements ControllerHelper{

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def list = InBox.createCriteria().list([max:max, offset:offset, sort: 'isRead']){
            and {
                eq("accepter", getCurrentUser())
            }
        }?.collect(){ InBox inBox ->
            def action
            def actualAction
            if(inBox.sourceType == SourceType.GD){
                action = inBox.action
                def flowList = WorkOrder.get(inBox.sourceId).flows
                def step = WorkOrder.get(inBox.sourceId).flowStep
                actualAction = step ? flowList[step-1].action : ''
            }
            if(inBox.sourceType == SourceType.YHZGD) {
                def HiddenRectificationStatus = ['DSH','DFK','YJJ','DYR','HG','BHG']
                action = HiddenRectificationStatus[inBox.action.toBigInteger()-1]
                actualAction = HiddenRectificationOrder.get(inBox.sourceId).status.name()
            }
            [id           : inBox.id
             , sourceId   : inBox.sourceId
             , sourceType : inBox.sourceType.cnName
             , title      : inBox.title
             , isRead     : inBox.isRead
             , dateCreated: inBox.dateCreated.format('yyyy-MM-dd HH:mm:ss ')
             , action     : action
             , actualAction: actualAction
            ]
        }
        def total = InBox.createCriteria().get {
            projections {
                count()
            }
            and {
                eq("accepter", getCurrentUser())
            }
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

    private withInBox(Long id,Closure c){
        InBox inBox = id ? InBox.get(id) : null
        if(inBox){
            c.call(inBox)
        }else{
            renderNoTFoundError()
        }
    }
}
