package com.hfyz.infoCenter

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.workOrder.WorkOrder

class InBoxController implements ControllerHelper{

    def list() {
        def type = request.JSON.type == 'GD'?SourceType.GD :(request.JSON.type == 'YHZGD'?SourceType.YHZGD:SourceType.DZLD)
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def list = InBox.createCriteria().list([max:max, offset:offset, sort: 'isRead']){
           and {
                eq("accepter", getCurrentUser())
            }
           and {
               eq("sourceType", type)
           }
        }?.collect(){ InBox inBox ->
            def flowList = WorkOrder.get(inBox.sourceId).flows
            def step = WorkOrder.get(inBox.sourceId).flowStep
            [id           : inBox.id
             , sourceId   : inBox.sourceId
             , title      : inBox.title
             , isRead     : inBox.isRead
             , dateCreated: inBox.dateCreated.format('yyyy-MM-dd HH:mm:ss ')
             , action     : step ? flowList[step-1].action : ''
            ]
        }
        def total = InBox.createCriteria().get {
            projections {
                count()
            }
            and {
                eq("accepter", getCurrentUser())
            }
            and {
                eq("sourceType", type)
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
