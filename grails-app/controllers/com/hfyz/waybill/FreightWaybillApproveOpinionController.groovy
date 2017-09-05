package com.hfyz.waybill

import com.commons.utils.ControllerHelper

class FreightWaybillApproveOpinionController implements ControllerHelper  {

    def approveOpinion() {
        withFreightWaybill(params.long('id')) { FreightWaybill freightWaybillInstance ->
            println '=====request.JSON====' + request.JSON
            FreightWaybillApproveOpinion freightWaybillApproveOpinion = new FreightWaybillApproveOpinion()
            freightWaybillApproveOpinion.approveTime = new Date()
            freightWaybillApproveOpinion.approveDesc = request.JSON.approveDesc
            freightWaybillApproveOpinion.approver = ''
            freightWaybillApproveOpinion.freightWaybill = freightWaybillInstance
//            freightWaybillApproveOpinion.save(flash: true)
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
