package com.hfyz.warning

import com.commons.utils.ControllerHelper

import grails.transaction.Transactional

@Transactional(readOnly = true)
class WarningController implements ControllerHelper {

    def warningService

    def list() {
        renderSuccessesWithMap([warningList: warningService.getWarningList(request.JSON.max, request.JSON.offset, request.JSON.frameNo, request.JSON.carLicenseNo)])
    }

    def view() {
        withWarning(params.long('id')) { warning ->
            renderSuccessesWithMap([warning: [id               : warning.id
                                              , frameNo        : warning.frameNo
                                              , carLicenseNo   : warning.carLicenseNo
                                              , carColor       : warning.carColor
                                              , warningSource  : warningService.getWarningSourceName(warning.warningSource)
                                              , warningType    : warning.warningType
                                              , warningTime    : warning.warningTime
                                              , warningTimes   : warning.warningTimes
                                              , superviseId    : warning.superviseId
                                              , endTime        : warning.endTime
                                              , superviseLevel : warning.superviseLevel
                                              , supervisePeople: warning.supervisePeople
                                              , supervisePhone : warning.supervisePhone
                                              , superviseEmail : warning.superviseEmail]])
        }
    }

    private withWarning(Long id, Closure c) {
        Warning warningInstance = id ? Warning.get(id) : null
        if (warningInstance) {
            c.call warningInstance
        } else {
            renderNoTFoundError()
        }

    }
}
