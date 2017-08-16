package com.hfyz.waybill

import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
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

    /**
     * 根据id得到实例
     */
    def showDetail(Long id) {
        PassLinePhysicalBasicInfo instance = id ? PassLinePhysicalBasicInfo.get(id) : null
        if (!instance) {
            throw new RecordNotFoundException()
        }

        def instanceMap=[
                lineCode             : instance.lineCode,
                lineName             : instance.lineName,
                modifyTime           : instance.modifyTime.format('yyyy-MM-dd'),
                businessArea         : instance.businessArea,
                lineType             : instance.lineType,
                startPlace           : instance.startPlace,
                endPlace             : instance.endPlace,
                mainPoint            : instance.mainPoint,
                startAdminDivsionCode: instance.startAdminDivsionCode,
                startAdminDivsionName: instance.startAdminDivsionName,
                endAdminDivsionCode  : instance.endAdminDivsionCode,
                endAdminDivsionName  : instance.endAdminDivsionName,
                lineMileAge          : instance.lineMileAge + 'km',
                highwayMileAge       : instance.highwayMileAge + 'km',
                percentage           : instance.percentage *100 + '%',
                highwayEntry         : instance.highwayEntry,
                highwayExit          : instance.highwayExit,
                highway              : instance.highway,
                villageLine          : instance.villageLine,
                travelLine           : instance.travelLine,
                busLine              : instance.busLine
        ]

        return instanceMap
    }




}
