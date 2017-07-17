package com.hfyz.support

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class OwnerCheckRecordController implements ControllerHelper{

    def index() { }

    def list() {
        def total = OwnerCheckRecord.count()
        def checkRecordList = OwnerCheckRecord.list( [max: PageUtils.getMax(request.JSON.max,10,100), offset: PageUtils.getOffset(request.JSON.offset)])?.collect{ recordObj->
            [id: recordObj.id
             ,auto: recordObj.auto
             ,companyCode: recordObj.companyCode
             ,dateCreated: recordObj.dateCreated.format("yyyy-MM-dd HH:mm:ss")
             ,question: recordObj.question
             ,answer: recordObj.answer
             ,responsed: recordObj.responsed
             ,operator: recordObj.operator?.name
             ,responseDate: recordObj.responseDate ? recordObj.responseDate.format("yyyy-MM-dd HH:mm:ss") : null
             ,responseContent: recordObj.responseContent
             ,responseTime: recordObj.responseTime]
        }
        renderSuccessesWithMap([checkRecordList: checkRecordList, total: total])
    }
}
