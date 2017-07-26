package com.hfyz

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
        def resultList = carService.networkRate(50)
        Date  date = new Date()
        resultList.each { result ->
            new Alarm(alarmType:  AlarmType.findByCodeNum('219')
                    , alarmLevel: AlarmLevel.NORMAL
                    , sourceType: SourceType.COMPANY
                    , sourceCode: result.ownerName
                    , alarmTime:  date-1
                    , updateTime: date-1).save(flush: true)
            new WorkOrder(sn:      result.ownerName + result.rate
                    , alarmType:   AlarmType.findByCodeNum('219')
                    , alarmLevel:  AlarmLevel.NORMAL
                    , companyCode: result.ownerName
                    , checkTime:   date-1
                    , rectificationTime: date).save(flush: true)
        }
        renderSuccess()
    }
}
