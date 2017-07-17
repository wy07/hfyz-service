package com.hfyz.support
import com.commons.utils.ControllerHelper

class OwnerCheckRecordController implements ControllerHelper{
    def ownerCheckRecordService
    def index() { }

    def list() {

        def  checkResult= ownerCheckRecordService.list(params.max, params.offset, params.company,
                                            params.startDate, params.endDate)
        renderSuccessesWithMap([checkResult: checkResult])

    }
}
