package com.hfyz.support

import grails.transaction.Transactional

@Transactional
class OwnerCheckRecordService {

    def list(def max, def offset, def company, def startDate, def endDate) {

        def sd = startDate == 'null'? null : new Date().parse('yyyy-MM-dd', startDate)
        def ed = endDate == null ? new Date() : new Date().parse('yyyy-MM-dd', endDate)
        def total = OwnerCheckRecord.createCriteria().get {
            projections {
                count()
            }
            like ("companyCode", "${company}%")
            if (!sd && ed) {
                and {
                    le("dateCreated", ed)
                }
            }
            if (sd && ed) {
                and {
                    between('dateCreated', sd, ed)
                }
            }
        }
        def checkRecordList = OwnerCheckRecord.createCriteria().list([max:max, offset:offset]){
            like ("companyCode", "${company}%")
            if (!sd && ed) {
                or {
                    le("dateCreated", ed)
                }
            }
            if (sd && ed) {
                or {
                    between('dateCreated', sd, ed)
                }
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
             ,responseTime: recordObj.responseTime ? ((recordObj.responseDate.getTime() - recordObj.dateCreated.getTime())/1000).setScale(2,BigDecimal.ROUND_HALF_UP) : null]
        }
        return [checkRecordList: checkRecordList, total: total]
    }
}
