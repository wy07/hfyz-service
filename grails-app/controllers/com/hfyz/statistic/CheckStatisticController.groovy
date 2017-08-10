package com.hfyz.statistic

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class CheckStatisticController implements ControllerHelper{

    def checkStatisticService

    def list(){
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        String [] tempStartArray = request.JSON.startDate.split("-")
        def dateLength = tempStartArray.length
        def startDate
        def endDate
        if(dateLength == 3){
            startDate = request.JSON.startDate
        }
        if(dateLength == 2){
            startDate = request.JSON.startDate + "-01"
            endDate = request.JSON.endDate + "-01"
        }

        if(request.JSON.startDate) {
            if (!request.JSON.startDate.contains("-")) {
                startDate = request.JSON.startDate + "-01-01"
                endDate = request.JSON.endDate + "-01-01"
            }
        }
        if(request.JSON.endDate){
            if (!request.JSON.endDate.contains("-")) {
                startDate = request.JSON.startDate + "-01-01"
                endDate = request.JSON.endDate + "-01-01"
            }
        }
        if(!request.JSON.endDate){
            endDate = request.JSON.endDate
        }
        if(!request.JSON.startDate){
            startDate = request.JSON.startDate
        }
        renderSuccessesWithMap(checkStatisticService.getCheckStatisticList(max,offset
        ,startDate,endDate,request.JSON.company,dateLength))
    }

}
