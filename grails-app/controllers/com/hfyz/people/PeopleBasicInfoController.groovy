package com.hfyz.people

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import grails.converters.JSON

import java.text.SimpleDateFormat

class PeopleBasicInfoController implements ControllerHelper {
    def peopleBasicInfoService
    /**
     * 列表查询
     * @return
     */
    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = peopleBasicInfoService.getPeopleList(request.JSON.type, request.JSON.name, request.JSON.phoneNo, request.JSON.IDCardNo, max, offset,currentUser)
        renderSuccessesWithMap([resultList: result.resultList, total: result.total])
    }

    /**
     * 查看详情
     */
    def more() {
        def result = peopleBasicInfoService.getDetailInfo(request.JSON.IDCardNo)
        renderSuccessesWithMap(result)
    }

    /**
     * 人员营运资格巡检
     * @return
     */
    def inspection() {
        peopleBasicInfoService.licenseInspection()
        renderSuccess()
    }
}