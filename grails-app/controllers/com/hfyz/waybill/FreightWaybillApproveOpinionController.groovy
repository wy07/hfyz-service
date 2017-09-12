package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.hfyz.infoCenter.SourceType

class FreightWaybillApproveOpinionController implements ControllerHelper  {

    def infoCenterService
    def approveOpinion() {
        withFreightWaybill(request.JSON.fid) { FreightWaybill freightWaybillInstance ->
            FreightWaybillApproveOpinion freightWaybillApproveOpinion = new FreightWaybillApproveOpinion()
            freightWaybillApproveOpinion.approveTime = new Date()
            freightWaybillApproveOpinion.approveDesc = request.JSON.approveDesc
            freightWaybillApproveOpinion.approver = currentUser
            freightWaybillApproveOpinion.freightWaybill = freightWaybillInstance
            freightWaybillApproveOpinion.save(flash: true)
            freightWaybillInstance.status = request.JSON.type == 'agreed' ? 'YJS' : 'YJJ'
            freightWaybillInstance.save(flush: true, failOnError: true)
            infoCenterService.save(freightWaybillInstance.id, SourceType.DZLD)
            renderSuccess()
        }
    }

    private withFreightWaybill(Long id, Closure c) {
        FreightWaybill freightWaybillInstance = id ? FreightWaybill.get(id) : null
        if (freightWaybillInstance) {
            c.call freightWaybillInstance
        } else {
            renderNoTFoundError()
        }
    }
}
