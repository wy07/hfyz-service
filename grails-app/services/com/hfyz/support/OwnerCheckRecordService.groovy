package com.hfyz.support

import grails.transaction.Transactional

@Transactional
class OwnerCheckRecordService {

    def list(def max, def offset, def company, def startDate, def endDate) {
        def sd = startDate ? new Date().parse('yyyy-MM-dd HH:mm', startDate) : null
        def ed = endDate ? new Date().parse('yyyy-MM-dd HH:mm', endDate) : new Date()
        def total = OwnerCheckRecord.createCriteria().get {
            projections {
                count()
            }
            if(company){
                like ("companyCode", "${company}%")
            }

            if(sd){
                ge("dateCreated", sd)
            }

            if(ed){
                le("dateCreated", ed)
            }
        }
        def checkRecordList = OwnerCheckRecord.createCriteria().list([max:max, offset:offset]){
            if(company){
                like ("companyCode", "${company}%")
            }

            if(sd){
                ge("dateCreated", sd)
            }

            if(ed){
                le("dateCreated", ed)
            }
        }?.collect(){ OwnerCheckRecord recordObj ->
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
        return [checkRecordList: checkRecordList, total: total]
    }
}
