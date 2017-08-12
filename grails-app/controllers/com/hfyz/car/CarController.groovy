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

        renderSuccessesWithMap([carNum        : 13202
                                , enterCarNum : 9890
                                , onlineCarNum: 6507])
    }

    def historyStatistic() {
        int currentYear = new Date().format('yyyy').toInteger()
        int year = NumberUtils.toInteger(request.JSON.year) ?: currentYear
        int month = currentYear == year ? new Date().format('MM').toInteger() : 12

        def initDate = {
            (1..month).collect{
                new Random().nextInt(100)
            }
        }
        def statistic = [enterRate:initDate()
                         ,onlineRate:initDate()
                         ,onlineTimeRate:initDate()
                         ,overspeedRate:initDate()
                         ,fatigueRate:initDate()
                         ,realTimeOnlineRate:initDate()]
        renderSuccessesWithMap([statistic: statistic])
    }
}
