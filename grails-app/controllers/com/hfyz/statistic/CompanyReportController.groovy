package com.hfyz.statistic

import com.commons.utils.ControllerHelper

class CompanyReportController implements ControllerHelper {

    def list() {
        if (getCurrentUser().isCompanyUser()) {
            renderSuccessesWithMap([resultList: [
                    [
                            "companyName"         : "安徽省合肥汽车客运有限公司",
                            "vehicleNetworkAccess": "99%",
                            "vehiclegoLive"       : "100%",
                            "vehicleOnline"       : "99%",
                            "vehicleOverspeed"    : "99%",
                            "overspeedHandle"     : "99%",
                            "driveTired"          : "99%",
                            "tiredHandle"         : "100%",
                            "platformDisconnect"  : "99%",
                            "dataUnqualified"     : "99%",
                            "checkResponse"       : "99%",
                            "totalScore"          : "99%"
                    ]], total                 : 1])
            return
        }

        def result = [[
                              "companyName"         : "安徽省合肥汽车客运有限公司",
                              "vehicleNetworkAccess": "99%",
                              "vehiclegoLive"       : "100%",
                              "vehicleOnline"       : "99%",
                              "vehicleOverspeed"    : "99%",
                              "overspeedHandle"     : "99%",
                              "driveTired"          : "99%",
                              "tiredHandle"         : "100%",
                              "platformDisconnect"  : "99%",
                              "dataUnqualified"     : "99%",
                              "checkResponse"       : "99%",
                              "totalScore"          : "99%"
                      ], [
                              "companyName"         : "合肥新亚汽车客运公司",
                              "vehicleNetworkAccess": "100%",
                              "vehiclegoLive"       : "99%",
                              "vehicleOnline"       : "99%",
                              "vehicleOverspeed"    : "99%",
                              "overspeedHandle"     : "99%",
                              "driveTired"          : "99%",
                              "tiredHandle"         : "99%",
                              "platformDisconnect"  : "99%",
                              "dataUnqualified"     : "100%",
                              "checkResponse"       : "99%",
                              "totalScore"          : "99%"
                      ]]
        renderSuccessesWithMap([resultList: result, total: 2])
    }
}
