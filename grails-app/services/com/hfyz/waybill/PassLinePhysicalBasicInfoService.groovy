package com.hfyz.waybill

import grails.transaction.Transactional

@Transactional
class PassLinePhysicalBasicInfoService {

     def search(String lineCode,String lineName,Integer max, Integer offset){
         def resultList = PassLinePhysicalBasicInfo.createCriteria().list([max: max, offset: offset, sort: 'id']){
             if (lineCode) {
                 eq("lineCode", lineCode)
             }
             if (lineName) {
                 eq("lineName", lineName)
             }
         }?.collect { PassLinePhysicalBasicInfo obj ->
             [ id: obj.id,
               lineCode : obj.lineCode,
               lineName : obj.lineName,
               businessArea : obj.businessArea,
               lineType : obj.lineType,
               startPlace : obj.startPlace,
               endPlace : obj.endPlace,
               mainPoint : obj.mainPoint,
               villageLine : obj.villageLine,
               travelLine : obj.travelLine,
               busLine :obj.busLine
             ]
         }

         def total = PassLinePhysicalBasicInfo.createCriteria().get {
             projections {
                 count()
             }
             if (lineCode) {
                 eq("lineCode", lineCode)
             }
             if (lineName) {
                 eq("lineName", lineName)
             }
         }

         return [resultList: resultList, total: total]
     }









}
