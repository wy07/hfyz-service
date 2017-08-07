package com.hfyz.roster

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class BlackListController implements ControllerHelper {
    def blackListService

    /**
     * 列表
     * @return
     */
    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def resultList = blackListService.showTableList(request.JSON.vehicleNo, request.JSON.dateBegin, request.JSON.dateEnd, max, offset)
        renderSuccessesWithMap(resultList)
    }

    /**
     * 根据id查询详情
     * @return
     */
    def show() {
        def result = blackListService.show(params.long('id'))
        renderSuccessesWithMap([instance: result])
    }

    /**
     * 保存
     */
    def save() {
        blackListService.save(request.JSON)
        renderSuccess()
    }

    /**
     * 删除
     */
    def delete() {
        blackListService.delete(params.long('id'))
        renderSuccess()
    }

    /**
     * 编辑页面获取信息
     */
    def edit() {
        BlackList instance = blackListService.getInstanceById(params.long('id'))
        def result = [
                id             : instance.id,
                blackType      : instance.blackType,
                controlBegin   : instance.controlBegin,
                controlBehavior: instance.controlBehavior,
                controlEnd     : instance.controlEnd,
                controlOrg     : instance.controlOrg,
                controlRange   : instance.controlRange,
                executor       : instance.executor,
                scheme         : instance.scheme,
                status         : instance.status.id,
                vehicleNo      : instance.vehicleNo
        ]
        renderSuccessesWithMap([instance: result])

    }

    /**
     * 更新
     */
    def update() {
        blackListService.update(params.long('id'), request.JSON)
        renderSuccess()
    }

}
