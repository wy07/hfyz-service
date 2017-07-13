package com.hfyz.support

import com.commons.utils.ControllerHelper

import java.text.SimpleDateFormat

class OwnerCheckRecordController implements ControllerHelper{

    SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss")

    def index() { }

    def list() {
        def checkRecordList = OwnerCheckRecord.list( [max: params.max, offset: params.offset])?.collect{recordObj->
            [id: recordObj.id
             ,auto: recordObj.auto
             ,companyCode: recordObj.companyCode
             ,dateCreated: sdf.format(recordObj.dateCreated)
             ,question: recordObj.question
             ,answer: recordObj.answer
             ,responsed: recordObj.responsed
             ,operator: recordObj.operator?.name
             ,responseDate: recordObj.responseDate ? sdf.format(recordObj.responseDate) : null
             ,responseContent: recordObj.responseContent
             ,responseTime: recordObj.responseTime
             ,total: OwnerCheckRecord.count()]
        }
        renderSuccessesWithMap([checkRecordList: checkRecordList])
    }
}
