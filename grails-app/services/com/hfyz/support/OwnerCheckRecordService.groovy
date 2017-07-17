package com.hfyz.support

import grails.transaction.Transactional

@Transactional
class OwnerCheckRecordService {

    def list(def max, def offset, def company, def startDate, def endDate) {

        def result = [:]
        def sd
        def ed
        if(dateConversion(startDate, endDate)) {
             sd = dateConversion(startDate, endDate).sd
             ed = dateConversion(startDate, endDate).ed
        }
        def total = OwnerCheckRecord.createCriteria().get {
            projections {
                count()
            }
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

        result << [checkRecordList: checkRecordList]
        result << [total: total]
        return result
    }

    def dateConversion (def startDate, def endDate) {
        def date = [:]
        Date sd
        Date ed
        if (startDate !='null' && endDate != null) {
            sd = new Date().parse('yyyy-MM-dd', startDate)
            ed = new Date().parse('yyyy-MM-dd', endDate)
            date << [sd: sd, ed: ed]
            return date
        }
        if (startDate =='null' && endDate != null) {
            sd =  null
            ed = new Date().parse('yyyy-MM-dd', endDate)
            date << [sd: sd, ed: ed]
            return date
        }
        if (startDate !='null' && endDate == null) {
            sd = new Date().parse('yyyy-MM-dd', startDate)
            ed = new Date()
            date << [sd: sd, ed: ed]
            return date
        }
    }
}
