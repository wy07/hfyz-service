package com.hfyz.rectification

import grails.transaction.Transactional

@Transactional
class HiddenRectificationOrderService {

    def getHiddenDangerList(def max, def offset ,def company ,def startDate, def endDate) {
        def sd = startDate ? new Date().parse('yyyy-MM-dd HH:mm',startDate) : null
        def ed = endDate ? new Date().parse('yyyy-MM-dd HH:mm',endDate) : null
        def total = HiddenRectificationOrder.createCriteria().get{
            projections {
                count()
            }
            if(company){
                like ("enterpirse", "${company}%")
            }

            if(sd){
                ge("inspectionDate", sd)
            }

            if(ed){
                le("inspectionDate", ed)
            }
        }
        def hiddenRectificationOrderList = HiddenRectificationOrder.createCriteria().list([max:max, offset:offset, sort:'id']){
            if(company){
                like('enterpirse',"${company}%")
            }
            if(sd){
                ge("inspectionDate", sd)
            }
            if(ed){
                le("inspectionDate", ed)
            }
        }?.collect{
            HiddenRectificationOrder obj ->
                [
                        id : obj.id,
                        billNo : obj.billNo,
                        enterpirse : obj.enterpirse,
                        examiner : obj.examiner,
                        inspectionDate : obj.inspectionDate.format('yyyy-MM-dd HH:mm:ss'),
                        dealineDate : obj.dealineDate.format('yyyy-MM-dd HH:mm:ss'),
                        status : obj.status.type
                ]
        }
        return [hiddenRectificationOrderList:hiddenRectificationOrderList,total:total]
    }

}
