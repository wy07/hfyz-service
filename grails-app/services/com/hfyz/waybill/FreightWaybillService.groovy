package com.hfyz.waybill

import com.hfyz.support.DangerousType
import grails.transaction.Transactional

@Transactional
class FreightWaybillService {

    def search(String vehicleNo, String ownerName, String dateBegin, String dateEnd, Integer max, Integer offset) {
        def begin = dateBegin ? Date.parse("yyyy-MM-dd HH:mm:ss", dateBegin) : ""
        def end = dateEnd ? Date.parse("yyyy-MM-dd HH:mm:ss", dateEnd) : ""

        def resultList = FreightWaybill.createCriteria().list([max: max, offset: offset]) {
            if (vehicleNo) {
                eq("vehicleNo", vehicleNo)
            }
            if (ownerName) {
                eq("ownerName", ownerName)
            }
            if (begin && end) {
                between("departTime", begin, end)
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
                    arriveArea   : bill.arriveArea
            ]

        })

        def total = FreightWaybill.createCriteria().get {
            projections {
                count()
            }
            if (vehicleNo) {
                eq("vehicleNo", vehicleNo)
            }
            if (ownerName) {
                eq("ownerName", ownerName)
            }
            if (begin && end) {
                between("departTime", begin, end)
            }
        }

        return [resultList: resultList, total: total]
    }
}
