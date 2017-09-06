package com.hfyz.infoCenter

import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.workOrder.WorkOrder

class InfoCenterService {

    def inBoxService
    def save(sourceId, sourceType) {
        InfoCenter infoCenter = new InfoCenter()
        infoCenter.sourceId = sourceId
        if(sourceType == SourceType.GD) {
            def workOrder = WorkOrder.get(sourceId)
            infoCenter.sourceType = SourceType.GD
            infoCenter.title = workOrder.ownerName + '工单，单号：' + workOrder.sn +
                    '，状态：' + workOrder.status.cnName + '，备注：' + workOrder.note
            infoCenter.save(flush: true, failOnError: true)
            inBoxService.saveWorkOrderInBox(infoCenter)

        }else if(sourceType == SourceType.YHZGD) {
            def hiddenRectificationOrder = HiddenRectificationOrder.get(sourceId)
            infoCenter.sourceType = SourceType.YHZGD
            infoCenter.title = hiddenRectificationOrder.enterprise + '整改，单号：' + hiddenRectificationOrder.billNo +
                    '，状态：' + hiddenRectificationOrder.status.type + '，整改内容：' + hiddenRectificationOrder.insDesc
            infoCenter.save(flush: true, failOnError: true)
            inBoxService.saveHiddenRectificationOrderInBox(infoCenter)
        }
    }
}
