package com.hfyz.warning

import com.commons.utils.SQLHelper
import grails.transaction.Transactional

@Transactional
class WarningService {

    def getWarningList(def max, def offset, String frameNo, String carLicenseNo) {
        def warningList = Warning.createCriteria().list([max: max, offset: offset]) {
            if (frameNo) {
                like("frameNo", "${frameNo}%")
            }
            if (carLicenseNo) {
                like("carLicenseNo", "${carLicenseNo}%")
            }
        }?.collect { Warning it ->
            [id             : it.id
             , frameNo      : it.frameNo
             , carLicenseNo : it.carLicenseNo
             , carColor     : it.carColor
             , warningSource: getWarningSourceName(it.warningSource)
             , warningType  : it.warningType
             , warningTime  : it.warningTime?.format('yyyy-MM-dd HH:mm:ss')
             , warningTimes : it.warningTimes]
        }

        def total = Warning.createCriteria().get {
            projections {
                count()
            }
            if (frameNo) {
                like("frameNo", "${frameNo}%")
            }
            if (carLicenseNo) {
                like("carLicenseNo", "${carLicenseNo}%")
            }
        }
        return [warningList: warningList, total: total]
    }

    def getWarningsAndTotalByCar(String frameNo, def max, def offset) {
        def warningList = getWarningsByCar(frameNo, max, offset)

        def total = Warning.createCriteria().get {
            projections {
                count()
            }
            eq("frameNo", frameNo)
        }
        return [warningList: warningList, total: total]
    }


    def getWarningsByCar(String frameNo, def max, def offset) {
        Warning.createCriteria().list([max: max, offset: offset, sort: 'warningTime', order: 'desc']) {
            eq("frameNo", frameNo)
        }?.collect { Warning it ->
            [id             : it.id
             , frameNo      : it.frameNo
             , carLicenseNo : it.carLicenseNo
             , carColor     : it.carColor
             , warningSource: getWarningSourceName(it.warningSource)
             , warningType  : it.warningType
             , warningTime  : it.warningTime?.format('yyyy-MM-dd HH:mm:ss')]
//             , warningTimes   : it.warningTimes]
        }
    }

    def getWarningsByCarAndDate(String frameNo,Date startDate,Date endDate){
        Warning.createCriteria().list([sort: 'warningTime', order: 'desc']) {
            eq("frameNo", frameNo)
            between("warningTime",startDate,endDate)
        }?.collect { Warning it ->
            [id             : it.id
             , frameNo      : it.frameNo
             , carLicenseNo : it.carLicenseNo
             , carColor     : it.carColor
             , warningSource: getWarningSourceName(it.warningSource)
             , warningType  : it.warningType
             , warningTime  : it.warningTime?.format('yyyy-MM-dd HH:mm:ss')]
        }
    }


    def final warningSourceNameMap = ['1': '车载终端', '2': '企业监控', '3': '政府监管', '9': '其他']

    def getWarningSourceName(warningSource) {
        return warningSourceNameMap["${warningSource}"]
    }
}
