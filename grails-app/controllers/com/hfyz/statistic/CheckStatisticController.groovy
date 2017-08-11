package com.hfyz.statistic

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class CheckStatisticController implements ControllerHelper{

    def checkStatisticService

    def list(){
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def startDate
        def endDate
        if(request.JSON.startDate){
            startDate = getRightDate(request.JSON.startDate)
        }
        if(request.JSON.endDate){
            endDate = getRightDate(request.JSON.endDate)
        }
        String [] tempStartArray = request.JSON.endDate?.split("-")
        def dateLength = tempStartArray.length
        renderSuccessesWithMap(checkStatisticService.getCheckStatisticList(max,offset
        ,startDate,endDate,request.JSON.company,dateLength))
    }

    def getRightDate(def date){
        def tempStart
        String [] tempStartArray = date.split("-")
        def dateLength = tempStartArray.length
        if(dateLength == 3){
            tempStart = date
        }
        if(dateLength == 2){
            tempStart = date + "-01"
        }
        if(dateLength != 3 && dateLength !=2 ){
            tempStart = date +"-01-01"
        }
        return tempStart
    }

}
