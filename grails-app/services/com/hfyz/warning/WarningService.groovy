package com.hfyz.warning

import com.commons.utils.SQLHelper
import grails.transaction.Transactional

@Transactional
class WarningService {

    def dataSource

    def getWarningmList() {
        def result = []
        def GET_WARNING_SQL = "select * from warning"
        SQLHelper.withDataSource(dataSource) { sql -> sql.rows(GET_WARNING_SQL.toString()) }
        def platformList = Warning.list()
        platformList.each {
            result << [id               : it.id
                       , frameNo        : it.frameNo
                       , carLicenseNo   : it.carLicenseNo
                       , carColor       : it.carColor
                       , warningSource  : getWarningSourceName(it.warningSource)
                       , warningType    : it.warningType
                       , warningTime    : it.warningTime
                       , warningTimes   : it.warningTimes
                       , superviseId    : it.superviseId
                       , endTime        : it.endTime
                       , superviseLevel : it.superviseLevel
                       , supervisePeople: it.supervisePeople
                       , supervisePhone : it.supervisePhone
                       , superviseEmail : it.superviseEmail]
        }
        return result
    }
//    条件查询
    def getWarningByCondition(String frameNo, String carLicenseNo, String warningType) {
        def warningList = Warning.createCriteria().list {
            if (frameNo) {
                like("frameNo", "${frameNo}%")
            }
            if (carLicenseNo) {
                like("carLicenseNo", "${carLicenseNo}%")
            }
            if (warningType) {
                like("warningType", "${warningType}%")
            }

        }?.collect { Warning it ->
            [id               : it.id
             , frameNo        : it.frameNo
             , carLicenseNo   : it.carLicenseNo
             , carColor       : it.carColor
             , warningSource  : getWarningSourceName(it.warningSource)
             , warningType    : it.warningType
             , warningTime    : it.warningTime
             , warningTimes   : it.warningTimes
             , superviseId    : it.superviseId
             , endTime        : it.endTime
             , superviseLevel : it.superviseLevel
             , supervisePeople: it.supervisePeople
             , supervisePhone : it.supervisePhone
             , superviseEmail : it.superviseEmail]

        }
        return warningList
    }

    def final warningSourceNameMap = ['1'  : '车载终端', '2': '企业监控', '3': '政府监管', '9': '其他']

    def getWarningSourceName(warningSource) {
        return warningSourceNameMap["${warningSource}"]
    }

}
