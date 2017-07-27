package com.hfyz

import com.commons.utils.ConfigUtil
import com.commons.utils.ControllerHelper
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
        def result = carService.search(request.JSON.businessType, request.JSON.licenseNo, max, offset)
        renderSuccessesWithMap([carList: result.carList, carCount: result.carCount])
    }

    def networkRate() {
        Date date = new Date()
        AlarmType alarmType = AlarmType.findByCodeNum('219')
        
        def resultList = carService.networkRate(ConfigUtil.instance.carRateAlarm as BigDecimal)
        resultList.each { result ->
            new Alarm(alarmType:  alarmType
                    , alarmLevel: AlarmLevel.NORMAL
                    , sourceType: SourceType.COMPANY
                    , sourceCode: result.companyCode
                    , alarmTime:  date
                    , updateTime: date).save(flush: true)
            new WorkOrder(sn:     System.currentTimeMillis() + ""
                    + System.nanoTime().toString().toString()[-6..-1]
                    + new Random().nextInt(100000).toString().padLeft(5, '0')
                    , alarmType:   alarmType
                    , alarmLevel:  AlarmLevel.NORMAL
                    , companyCode: result.companyCode
                    , checkTime:   date
                    , rectificationTime: date.plus(5)).save(flush: true)
        }
        renderSuccessesWithMap([resultList: resultList])
    }
}
