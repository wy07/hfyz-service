package com.hfyz.owner

import com.commons.utils.ControllerHelper
import com.hfyz.support.AlarmType
import com.hfyz.warning.Alarm
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.SourceType
import com.hfyz.workOrder.WorkOrder

/**
 * 企业内部管理制度
 */
class CompanyRegulationController implements ControllerHelper {

    def companyRegulationService

    //查询公司是否有上传制度附件
    def patrolCompanyRegulation() {
        def date = new Date()
        companyRegulationService.patrolCompanyRegulation()?.each { obj ->
            new Alarm(alarmType: AlarmType.findByCodeNum('205')
                    , alarmLevel: AlarmLevel.NORMAL
                    , sourceType: SourceType.COMPANY
                    , sourceCode: obj.companyCode
                    , alarmTime: date
                    , updateTime: date
                    , dateCreated: date
                    , note: '运营企业内部管理制度未上传,请在5天内整改').save(flush: true)

            def sn = System.currentTimeMillis() + "" + System.nanoTime().toString().toString()[-6..-1] + new Random().nextInt(100000).toString().padLeft(5, '0')
            new WorkOrder(sn: sn
                    , alarmType: AlarmType.findByCodeNum('205')
                    , alarmLevel: AlarmLevel.NORMAL
                    , companyCode: obj.companyCode
                    , ownerName: obj.ownerName
                    , operateManager: obj.operateManager
                    , phone: obj.phone
                    , checkTime: date
                    , rectificationTime: date + 5
                    , note: '业户').save(flush: true)
        }
        renderSuccess()

    }


}
