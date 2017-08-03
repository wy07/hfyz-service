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
        def resultList = whiteListService.list(request.JSON.vehicleNo, max, offset)
        renderSuccessesWithMap(resultList)
    }

    /**
     * 根据id查询详情
     * @return
     */
    def more() {
        def result = whiteListService.more(params.long('id'))
        renderSuccessesWithMap(result)
    }

    /**
     * 保存
     */
    def save() {
        def msg = whiteListService.save(request.JSON)
        if (msg) {
            renderErrorMsg(msg)
        }
        renderSuccess()
    }

    /**
     * 删除
     */
    def delete() {
        whiteListService.delete(params.id)
        renderSuccess()
    }

    def get() {
        def instance = WhiteList.get(params.id)
        if (instance) {
            def result = [
                    id       : instance.id,
                    vehicleNo: instance.vehicleNo
            ]
            renderSuccessesWithMap(result)
        }
        renderNoTFoundError()
    }

    /**
     * 更新
     */
    def update() {
        if (whiteListService.update(params.id, request.JSON)) {
            renderSuccess()
        }
        renderErrorMsg("更新失败，请重试！")
    }
}
