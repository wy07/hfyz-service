package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.security.User

class PassLineBusinessBasicInfoController implements ControllerHelper {
    def passLineBusinessBasicInfoService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = passLineBusinessBasicInfoService.search(request.JSON.ownerName, currentUser, max, offset)
        renderSuccessesWithMap(result)
    }

    def show() {
        withPassLineBusinessBasicInfo(params.long('id')) { PassLineBusinessBasicInfo basicInfoInstance ->
            renderSuccessesWithMap([passLineBusinessBasicInfo: [id                       : basicInfoInstance.id
                                                     , lineCode               : basicInfoInstance.lineCode
                                                     , ownerName              : basicInfoInstance.ownerName
                                                     , companyCode            : basicInfoInstance.companyCode
                                                     , licenseCharacter       : basicInfoInstance.licenseCharacter
                                                     , licenseNo              : basicInfoInstance.licenseNo
                                                     , busType                : basicInfoInstance.busType
                                                     , startStationName       : basicInfoInstance.startStationName
                                                     , endStationName         : basicInfoInstance.endStationName
                                                     , stopStation            : basicInfoInstance.stopStation
                                                     , mainPoint              : basicInfoInstance.mainPoint
                                                     , dayTimes               : basicInfoInstance.dayTimes
                                                     , businessWay            : basicInfoInstance.businessWay
                                                     , licenseDecideBookNo    : basicInfoInstance.licenseDecideBookNo
                                                     , decideTime             : basicInfoInstance.decideTime?.format('yyyy-MM-dd')
                                                     , decideOrc              : basicInfoInstance.decideOrc
                                                     , beginTime              : basicInfoInstance.beginTime?.format('yyyy-MM-dd')
                                                     , endTime                : basicInfoInstance.endTime?.format('yyyy-MM-dd')
                                                     , licenseType            : basicInfoInstance.licenseType
                                                     , businessSituation      : basicInfoInstance.businessSituation
                                                     , changeLicenseTimes     : basicInfoInstance.changeLicenseTimes
                                                     , generalinfoChangeTimes : basicInfoInstance.generalinfoChangeTimes
                                                     , businessinfoChangeTimes: basicInfoInstance.businessinfoChangeTimes
                                                     , inputTotalCar          : basicInfoInstance.inputTotalCar
                                                     , inputTotalSeat         : basicInfoInstance.inputTotalSeat
                                                     , totalLinePlate         : basicInfoInstance.totalLinePlate]
            ])
        }

    }

    private withPassLineBusinessBasicInfo(Long id, Closure c) {
        PassLineBusinessBasicInfo passLineBusinessBasicInfoInstance = id ? PassLineBusinessBasicInfo.get(id) : null
        if (passLineBusinessBasicInfoInstance) {
            c.call passLineBusinessBasicInfoInstance
        } else {
            renderNoTFoundError()
        }
    }

    private withCompanyPassLineBusinessBasicInfo(Long id, User user, Closure c) {
        PassLineBusinessBasicInfo passLineBusinessBasicInfoInstance = id ? PassLineBusinessBasicInfo.get(id) : null
        if (passLineBusinessBasicInfoInstance) {
            if (passLineBusinessBasicInfoInstance.companyCode != user.companyCode) {
                renderNoInstancePermError()
            } else {
                c.call passLineBusinessBasicInfoInstance
            }
        } else {
            renderNoTFoundError()
        }
    }
}
