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
        println(params)
        int max = PageUtils.getMax(params.max, 10, 100)
        int offset = PageUtils.getOffset(params.offset)
        def result = peopleBasicInfoService.getPeopleList(params.name, params.phoneNo, params.IDCardNo, max, offset)
        println(result)
        renderSuccessesWithMap([resultList: result.resultList, total: result.total])
    }

    /**
     * 查看详情
     */
    def more() {
        def result = peopleBasicInfoService.getDetailInfo(params.IDCardNo)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        JSON.registerObjectMarshaller(java.sql.Timestamp) { o -> sdf.format(o) }
        renderSuccessesWithMap(result)
    }
}