package com.hfyz.waybill

import com.hfyz.security.User
import grails.transaction.Transactional

@Transactional
class FreightWaybillService {


//    String vehicleNo, String ownerName, String dateBegin, String dateEnd
    def search(Map inputParams, User user, Integer max, Integer offset) {
        def begin = inputParams.dateBegin ? Date.parse("yyyy-MM-dd HH:mm:ss", inputParams.dateBegin) : ""
        def end = inputParams.dateEnd ? Date.parse("yyyy-MM-dd HH:mm:ss", inputParams.dateEnd) : ""


        def resultList = FreightWaybill.createCriteria().list([max: max, offset: offset]) {
            if (user.isCompanyUser()) {
                eq('companyCode', user.companyCode)
            } else if (inputParams.ownerName) {
                like("ownerName", "${inputParams.ownerName}%")
            }

            if (inputParams.vehicleNo) {
                like("vehicleNo", "${inputParams.vehicleNo}%")

            }

            if (begin && end) {
                between("departTime", begin, end)
            }
            if(userCompanyCode){
                eq("companyCode",userCompanyCode)
            }
        }?.collect({ FreightWaybill bill ->
            [
                    id           : bill.id,
                    vehicleNo    : bill.vehicleNo,
                    ownerName    : bill.ownerName,
                    dangerousName: bill.dangerousName,
                    dangerousType: bill.dangerousType?.name,
                    amount       : bill.amount,
                    loadedType   : bill.loadedType,
                    departTime   : bill.departTime?.format("yyyy-MM-dd HH:mm:ss"),
                    fullLoaded   : bill.fullLoaded,
                    departArea   : bill.departArea,
                    arriveArea   : bill.arriveArea,
                    startProvince: bill.startProvince,
                    startCity    : bill.startCity,
                    startDistrict: bill.startDistrict,
                    endProvince  : bill.endProvince,
                    endCity      : bill.endCity,
                    endDistrict  : bill.endDistrict
            ]

        })

        def total = FreightWaybill.createCriteria().get {
            projections {
                count()
            }
            if (user.isCompanyUser()) {
                eq('companyCode', user.companyCode)
            } else if (inputParams.ownerName) {
                eq("ownerName", inputParams.ownerName)
            }
            if (inputParams.vehicleNo) {
                eq("vehicleNo", inputParams.vehicleNo)
            }
            if (begin && end) {
                between("departTime", begin, end)
            }
            if(userCompanyCode){
                eq("companyCode",userCompanyCode)
            }
        }

        return [resultList: resultList, total: total]
    }
}
