package com.hfyz.owner

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.support.AlarmType
import com.hfyz.warning.Alarm
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.SourceType
import com.hfyz.workOrder.WorkOrder
import grails.converters.JSON
import org.springframework.web.multipart.MultipartHttpServletRequest

/**
 * 企业内部管理制度
 */
class CompanyRegulationController implements ControllerHelper {

    def companyRegulationService

    /**
     * 列表
     */
    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def requestParams = [
                ownerName: request.JSON.ownerName,
                dateBegin: request.JSON.dateBegin,
                dateEnd  : request.JSON.dateEnd
        ]
        def resultList = companyRegulationService.search(requestParams, max, offset, currentUser)
        renderSuccessesWithMap(resultList)
    }



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

    def save(){
        def upload = request.getFile('upload')
        def originalFilename = upload.originalFilename

        CompanyRegulation companyRegulation = new CompanyRegulation()
        companyRegulation.ownerName = OwnerIdentity.findByCompanyCode(getCurrentUser()?.companyCode).ownerName
        companyRegulation.companyCode = getCurrentUser()?.companyCode
        companyRegulation.regulationName = params.regulationName
        companyRegulation.fileName = originalFilename.substring(0, originalFilename.lastIndexOf('.'))
        companyRegulation.fileType = originalFilename.substring(originalFilename.lastIndexOf('.')+1, originalFilename.length())
        companyRegulation.fileSize = (upload.getSize()/1024).setScale(2,BigDecimal.ROUND_HALF_UP)

        companyRegulation.save(flush: true, failOnError: true)
        renderSuccess()
    }
}
