package com.hfyz.infoCenter

import com.hfyz.rectification.HiddenRectificationOrder
import com.hfyz.waybill.FreightWaybill
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
            inBoxService.saveHiddenRectificationOrderOrFreightWaybillInBox(infoCenter)

        }else if(sourceType == SourceType.DZLD) {
            def freightWaybill = FreightWaybill.get(sourceId)
            def status = freightWaybill.status == 'SHZ'? '审核中' : (freightWaybill.status == 'YJJ'? '审核拒绝' : '审核通过')
            infoCenter.sourceType = SourceType.DZLD
            infoCenter.title = freightWaybill.ownerName + '电子路单，车架号：' + freightWaybill.frameNo +
                    '，状态：' + status + '，危险品名称：' + freightWaybill.dangerousName +
                    '，路线：' +freightWaybill.routerName
            infoCenter.save(flush: true, failOnError: true)
            inBoxService.saveHiddenRectificationOrderOrFreightWaybillInBox(infoCenter)
        }
    }
}
