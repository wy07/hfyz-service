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
        def resultList = blackListService.list(request.JSON.vehicleNo, request.JSON.dateBegin, request.JSON.dateEnd, max, offset)
        renderSuccessesWithMap(resultList)
    }

    /**
     * 根据id查询详情
     * @return
     */
    def more() {
        def result = blackListService.more(params.id)
        if (result) {
            renderSuccessesWithMap(result)
        }
        renderNoTFoundError()
    }

    /**
     * 保存
     */
    def save() {
        def msg = blackListService.save(request.JSON)
        if (msg) {
            renderErrorMsg(msg)
        }
        renderSuccess()
    }

    /**
     * 删除
     */
    def delete() {
        blackListService.delete(params.id)
        renderSuccess()
    }

    /**
     * 编辑获取当前记录所有信息
     */
    def get() {
        def instance = BlackList.get(params.id)
        if (instance) {
            def result = [
                    id          : instance.id,
                    vehicleNo   : instance.vehicleNo,
                    controlBegin: instance.controlBegin,
                    controlEnd  : instance.controlEnd,
            ]
            renderSuccessesWithMap(result)
        }
        renderNoTFoundError()
    }

    /**
     * 更新
     */
    def update() {
        if (blackListService.update(params.id, request.JSON)) {
            renderSuccess()
        }
        renderErrorMsg("更新失败，请重试！")
    }

}
