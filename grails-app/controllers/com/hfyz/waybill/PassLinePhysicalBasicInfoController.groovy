package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import grails.converters.JSON

class PassLinePhysicalBasicInfoController implements ControllerHelper{
    def passLinePhysicalBasicInfoService
    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = passLinePhysicalBasicInfoService.search(request.JSON.lineCode, request.JSON.lineName, max, offset)
        renderSuccessesWithMap(result)
    }

    def show() {
        PassLinePhysicalBasicInfo instance = passLinePhysicalBasicInfoService.getInstanceById(params.long('id'))
        renderSuccessesWithMap([instance: instance])
    }
    


}
