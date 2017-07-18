package com.hfyz.warning

import com.commons.utils.ControllerHelper

import grails.transaction.Transactional

@Transactional(readOnly = true)
class WarningController implements ControllerHelper {

    def warningService

    def list() {
        renderSuccessesWithMap([warningList: warningService.getWarningmList()])
    }

    def search() {
        renderSuccessesWithMap([warningList: warningService.getWarningByCondition(params.frameNo, params.carLicenseNo, params.warningType)])
    }

    def view() {
        withWarning(params.long('id')) { warning ->
            renderSuccessesWithMap([warning: [id               : warning.id
                                              , frameNo        : warning.frameNo
                                              , carLicenseNo   : warning.carLicenseNo
                                              , carColor       : warning.carColor
                                              , warningSource  : getWarningSourceName(warning.warningSource)
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

    def final warningSourceNameMap = ['1'  : '车载终端', '2': '企业监控', '3': '政府监管', '9': '其他']

    def getWarningSourceName(warningSource) {

        warningSourceNameMap['${warningSource}']
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
