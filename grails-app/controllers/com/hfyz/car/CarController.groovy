package com.hfyz.car

import com.commons.utils.ConfigUtil
import com.commons.utils.ControllerHelper
import com.commons.utils.NumberUtils
import com.commons.utils.PageUtils
import com.hfyz.support.AlarmType
import com.hfyz.warning.Alarm
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.SourceType
import com.hfyz.workOrder.WorkOrder

class CarController implements ControllerHelper {
    def carService

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

    def getCompanyCars(){
        if(!params.companyCode){
            renderErrorMsg('请选择要查询的企业')
            return
        }
        renderSuccessesWithMap([cars:carService.getCompanyCars(params.companyCode)])
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
        renderSuccessesWithMap([statistic: carService.historyStatistic(currentUser.org,NumberUtils.toInteger(request.JSON.year))])
    }

    def getCarInfo() {
        def carInfo = CarBasicInfo.findByLicenseNo(request.JSON.vehicleNo)
        if(carInfo) {
            carInfo = [
                    carPlateColor:carInfo?.carPlateColor,
                    carType:carInfo?.carType,
                    carSize:"${carInfo?.carLength}mm*${carInfo?.carWidth}mm*${carInfo?.carHeight}"
            ]
        }else {
            carInfo = []
        }
        renderSuccessesWithMap([carInfo: carInfo])
    }
}
