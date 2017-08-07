package com.hfyz.roster

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class WhiteListController implements ControllerHelper {
    def whiteListService

    /**
     * 列表
     * @return
     */
    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def resultList = whiteListService.showTableList(request.JSON.vehicleNo, max, offset)
        renderSuccessesWithMap(resultList)
    }

    /**
     * 根据id查询详情
     * @return
     */
    def show() {
        def result = whiteListService.show(params.long('id'))
        renderSuccessesWithMap([instance:result])
    }

    /**
     * 保存
     */
    def save() {
        whiteListService.save(request.JSON)
        renderSuccess()
    }

    /**
     * 删除
     */
    def delete() {
        whiteListService.delete(params.long('id'))
        renderSuccess()
    }

    /**
     * 编辑
     * @return
     */
    def edit() {
        WhiteList instance = whiteListService.getInstanceById(params.long('id'))
        renderSuccessesWithMap([instance: instance])
    }

    /**
     * 更新
     */
    def update() {
        whiteListService.update(params.long('id'), request.JSON)
        renderSuccess()
    }
}
