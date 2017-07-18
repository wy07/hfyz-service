package com.hfyz.support
import com.commons.utils.ControllerHelper

class OwnerCheckRecordController implements ControllerHelper{
    def ownerCheckRecordService
    def index() { }

    def list() {

        def  checkResult= ownerCheckRecordService.list(request.JSON.max, request.JSON.offset, request.JSON.company,
                                request.JSON.startDate, request.JSON.endDate)
        renderSuccessesWithMap([checkResult: checkResult])

    }
}
