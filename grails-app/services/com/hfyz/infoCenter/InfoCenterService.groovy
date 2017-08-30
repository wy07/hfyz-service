package com.hfyz.infoCenter

import com.hfyz.workOrder.WorkOrder

class InfoCenterService {

    def inBoxService
    def save(sourceId, sourceType) {
        InfoCenter infoCenter = new InfoCenter()
        infoCenter.sourceId = sourceId
        if(sourceType == 'GD') {
            infoCenter.sourceType = SourceType.GD
            infoCenter.title = '工单消息：' + WorkOrder.get(sourceId).sn
        }
        infoCenter.save(flush: true, failOnError: true)
        inBoxService.save(infoCenter)
    }
}
