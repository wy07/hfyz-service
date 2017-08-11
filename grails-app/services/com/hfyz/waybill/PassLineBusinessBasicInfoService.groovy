package com.hfyz.waybill

import grails.transaction.Transactional

@Transactional
class PassLineBusinessBasicInfoService {

    def search(String ownerName, Integer max, Integer offset) {

        def resultList = PassLineBusinessBasicInfo.createCriteria().list([max: max, offset: offset]) {
            if (ownerName) {
                eq("ownerName", ownerName)
            }
        }?.collect({ PassLineBusinessBasicInfo info ->
            [
                    id                     : info.id,
                    ownerName              : info.ownerName,
                    licenseCharacter       : info.licenseCharacter,
                    licenseNo              : info.licenseNo,
                    busType                : info.busType,
                    startStationName       : info.startStationName,
                    endStationName         : info.endStationName,
                    stopStation            : info.stopStation,
                    mainPoint              : info.mainPoint,
                    dayTimes               : info.dayTimes,
                    businessWay            : info.businessWay,
                    licenseDecideBookNo    : info.licenseDecideBookNo,
                    decideTime             : info.decideTime.format('yyyy-MM-dd'),
                    decideOrc              : info.decideOrc,
                    beginTime              : info.beginTime,
                    endTime                : info.endTime,
                    licenseType            : info.licenseType,
                    businessSituation      : info.businessSituation,
                    changeLicenseTimes     : info.changeLicenseTimes,
                    generalinfoChangeTimes : info.generalinfoChangeTimes,
                    businessinfoChangeTimes: info.businessinfoChangeTimes,
                    inputTotalCar          : info.inputTotalCar,
                    inputTotalSeat         : info.inputTotalSeat,
                    totalLinePlate         : info.totalLinePlate,
            ]
        })

        def total = PassLineBusinessBasicInfo.createCriteria().get {
            projections {
                count()
            }
            if (ownerName) {
                eq("ownerName", ownerName)
            }
        }

        return [resultList: resultList, total: total]
    }
}
