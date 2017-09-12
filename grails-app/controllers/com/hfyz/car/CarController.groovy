package com.hfyz.car

import com.commons.utils.ConfigUtil
import com.commons.utils.ControllerHelper
import com.commons.utils.NumberUtils
import com.commons.utils.PageUtils
import com.hfyz.support.AlarmType
import com.hfyz.warning.Alarm
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.SourceType
import com.hfyz.warning.Warning
import com.hfyz.workOrder.WorkOrder

class CarController implements ControllerHelper {
    def carService
    def warningService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = carService.search(
                [businessType: request.JSON.businessType
                 , licenseNo : request.JSON.licenseNo
                 , dateBegin : request.JSON.dateBegin
                 , dateEnd   : request.JSON.dateEnd]
                , currentUser
                , max
                , offset)
        renderSuccessesWithMap([carList: result.carList, carCount: result.carCount])
    }

    def getCompanyCars() {
        if (!params.companyCode) {
            renderErrorMsg('请选择要查询的企业')
            return
        }
        renderSuccessesWithMap([cars: carService.getCompanyCars(params.companyCode)])
    }

    def networkRate() {
        Date date = new Date()
        AlarmType alarmType = AlarmType.findByCodeNum('219')

        def resultList = carService.networkRate(ConfigUtil.instance.carRateAlarm as BigDecimal)
        resultList.each { result ->
            new Alarm(alarmType: alarmType
                    , alarmLevel: AlarmLevel.NORMAL
                    , sourceType: SourceType.COMPANY
                    , sourceCode: result.companyCode
                    , alarmTime: date
                    , updateTime: date).save(flush: true)
            new WorkOrder(sn: System.currentTimeMillis() + ""
                    + System.nanoTime().toString().toString()[-6..-1]
                    + new Random().nextInt(100000).toString().padLeft(5, '0')
                    , alarmType: alarmType
                    , alarmLevel: AlarmLevel.NORMAL
                    , companyCode: result.companyCode
                    , ownerName: result.ownerName
                    , operateManager: result.operateManager
                    , phone: result.phone
                    , checkTime: date
                    , rectificationTime: date.plus(5)).save(flush: true)
        }
        renderSuccessesWithMap([resultList: resultList])
    }

    def carNumStatistic() {

        renderSuccessesWithMap(carService.carNumStatistic())
    }

    def historyStatistic() {
        renderSuccessesWithMap([statistic: carService.historyStatistic(currentUser.org, NumberUtils.toInteger(request.JSON.year))])
    }

    def detail() {
        withCar(params.long('id')) { carInstance ->
            renderSuccessesWithMap([car: [id                : carInstance.id
                                          , modifyTime      : carInstance.modifyTime?.format('yyyy-MM-dd HH:mm:ss')
                                          , licenseNo       : carInstance.licenseNo
                                          , carPlateColor   : carInstance.carPlateColor
                                          , brand           : carInstance.brand
                                          , model           : carInstance.model
                                          , carType         : carInstance.carType
                                          , passengerLevel  : carInstance.passengerLevel
                                          , carColor        : carInstance.carColor
                                          , engNo           : carInstance.engNo
                                          , frameNo         : carInstance.frameNo
                                          , carIdentityCode : carInstance.carIdentityCode
                                          , seatNo          : carInstance.seatNo
                                          , carTonnage      : carInstance.carTonnage
                                          , carBoxNo        : carInstance.carBoxNo
                                          , volume          : carInstance.volume
                                          , fuelType        : carInstance.fuelType
                                          , engPower        : carInstance.engPower
                                          , leaveFactoryTime: carInstance.leaveFactoryTime?.format('yyyy-MM-dd HH:mm:ss')
                                          , buyCarTime      : carInstance.buyCarTime?.format('yyyy-MM-dd HH:mm:ss')
                                          , settleTime      : carInstance.settleTime?.format('yyyy-MM-dd HH:mm:ss')
                                          , picture         : carInstance.picture
                                          , wheelbase       : carInstance.wheelbase
                                          , carLength       : carInstance.carLength
                                          , carHeight       : carInstance.carHeight
                                          , carWidth        : carInstance.carWidth
                                          , carSmokeNo      : carInstance.carSmokeNo
                                          , leafSpringNo    : carInstance.leafSpringNo
                                          , tractionTonnage : carInstance.tractionTonnage]])
        }
    }

    def getWarning() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        withCar(params.long('id')) { carInstance ->
            renderSuccessesWithMap(warningService.getWarningsAndTotalByCar(carInstance.frameNo, max, offset))
        }
    }

    def getHistory() {
        withCar(params.long('id')) { carInstance ->
            def historyList = [
                    [licenseNo : carInstance.licenseNo, carColor: carInstance.carColor, point: '116.37168,39.93218', speed: '80', totalMileage: 3, recSpeed: 80, direction: 4
                     , altitude: 75, vehicleState: '良好', refreshTime: new Date().format('yyyy-MM-dd')],
                    [licenseNo : carInstance.licenseNo, carColor: carInstance.carColor, point: '118.64859,39.93218', speed: '75', totalMileage: 1, recSpeed: 75, direction: 9
                     , altitude: 74.6, vehicleState: '良好', refreshTime: new Date().format('yyyy-MM-dd')],
                    [licenseNo : carInstance.licenseNo, carColor: carInstance.carColor, point: '116.98514,45.93218', speed: '60', totalMileage: 2.8, recSpeed: 60, direction: 6
                     , altitude: 75, vehicleState: '良好', refreshTime: new Date().format('yyyy-MM-dd')],
                    [licenseNo : carInstance.licenseNo, carColor: carInstance.carColor, point: '120.37168,39.93218', speed: '20', totalMileage: 3.4, recSpeed: 20, direction: 7
                     , altitude: 78, vehicleState: '良好', refreshTime: new Date().format('yyyy-MM-dd')],
                    [licenseNo : carInstance.licenseNo, carColor: carInstance.carColor, point: '116.37168,39.75249', speed: '48', totalMileage: 0.7, recSpeed: 48, direction: 10
                     , altitude: 77, vehicleState: '良好', refreshTime: new Date().format('yyyy-MM-dd')]]
            renderSuccessesWithMap([historyList: historyList])
        }
    }

    def getCarInfo() {
        def carInfo = CarBasicInfo.findByLicenseNo(request.JSON.vehicleNo)
        if (carInfo) {
            carInfo = [
                    carPlateColor: carInfo?.carPlateColor,
                    carType      : carInfo?.carType,
                    carSize      : "${carInfo?.carLength}mm*${carInfo?.carWidth}mm*${carInfo?.carHeight}"
            ]
        } else {
            carInfo = []
        }
        renderSuccessesWithMap([carInfo: carInfo])
    }

    def warningAndHistoryList() {
        def result = carService.getWarningAndHistoryList(request.JSON.licenseNo, currentUser, PageUtils.getMax(request.JSON.max, 10, 100))
        renderSuccessesWithMap([warnings          : result.warnings
                                , historyLocations: result.historyLocations])
    }

    def historyInfo() {
        Date startDate = request.JSON.startDate ? new Date().parse('yyyy-MM-dd HH:mm', request.JSON.startDate) : null
        if (!startDate) {
            renderErrorMsg('请输入开始时间！')
            return false
        }
        Date endDate = request.JSON.endDate ? new Date().parse('yyyy-MM-dd HH:mm', request.JSON.endDate) : null
        if (!startDate) {
            renderErrorMsg('请输入结束时间！')
            return false
        }
        if (endDate < startDate) {
            renderErrorMsg('结束时间不能小于开始时间.')
            return false
        }
        def result = carService.getHistoryInfo(request.JSON.licenseNo, currentUser, startDate, endDate)
        renderSuccessesWithMap([warnings          : result.warnings
                                , historyLocations: result.historyLocations])
    }

    private withCar(Long id, Closure c) {
        CarBasicInfo carInstance = id ? CarBasicInfo.get(id) : null
        if (carInstance) {
            c.call carInstance
        } else {
            renderNoTFoundError()
        }
    }
}
