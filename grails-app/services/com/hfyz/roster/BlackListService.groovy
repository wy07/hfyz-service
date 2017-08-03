package com.hfyz.roster

import com.hfyz.car.CarBasicInfo
import grails.transaction.Transactional

@Transactional
class BlackListService {

    /**
     * 列表查询
     * @param vehicleNo 车辆号牌
     * @param dateBegin 起始时间
     * @param dateEnd 结束时间
     * @param max
     * @param offset
     */
    def list(vehicleNo, dateBegin, dateEnd, max, offset) {
        def total = BlackList.createCriteria().get {
            projections {
                count()
            }
            if (vehicleNo) {
                eq("vehicleNo", "${vehicleNo}")
            }
        }

        def begin = dateBegin ? Date.parse("yyyy-MM-dd HH:mm:ss", dateBegin) : ""
        def end = dateEnd ? Date.parse("yyyy-MM-dd HH:mm:ss", dateEnd) : ""

        def resultList = BlackList.createCriteria().list(max: max, offset: offset) {
            if (vehicleNo) {
                eq("vehicleNo", "${vehicleNo}")
            }
            if (begin && end) {
                between("controlBegin", begin, end)
            }
        }?.collect { BlackList blackList ->
            [
                    id          : blackList.id,
                    vehicleNo   : blackList.vehicleNo,
                    controlBegin: blackList.controlBegin?.format('yyyy-MM-dd'),
                    controlEnd  : blackList.controlEnd?.format('yyyy-MM-dd'),
            ]
        }
        return [resultList: resultList, total: total]
    }

    /**
     * 查看详情
     */
    def more(id) {
        def instance = id ? BlackList.get(id) : null
        def vehicle = CarBasicInfo.findByLicenseNo(instance.vehicleNo)
        if (instance && vehicle) {
            def result = [
                    id           : instance.id,
                    vehicleNo    : instance.vehicleNo,
                    controlBegin : instance.controlBegin?.format("yyyy-MM-dd HH:mm:ss"),
                    controlEnd   : instance.controlEnd?.format("yyyy-MM-dd HH:mm:ss"),
                    carType      : vehicle.carType,
                    carPlateColor: vehicle.carPlateColor,
                    carColor     : vehicle.carColor
            ]
            return result
        }

        return null
    }

    /**
     * 更新
     * @param id
     * @param obj 请求json串
     */
    def update(id, obj) {
        def blackList = id ? BlackList.get(id) : null
        if (!blackList) {
            return false
        }
        blackList.controlBegin = Date.parse("yyyy-MM-dd HH:mm:ss", obj.controlBegin)
        blackList.controlEnd = Date.parse("yyyy-MM-dd HH:mm:ss", obj.controlEnd)
        blackList.save(flush: true, failOnError: true)
        return true
    }

    /**
     * 保存黑名单信息，验证车牌号是否存在，如不存在，保存不成功
     * @param obj
     */
    def save(obj) {
        def carBasicInfo = CarBasicInfo.findByLicenseNo(obj.vehicleNo)
        if (!carBasicInfo) {
            return '车辆号牌不存在'
        }
        BlackList blackList = new BlackList(obj)
        blackList.save(flush: true, failOnError: true)
        return
    }

    /**
     * 删除黑名单信息记录
     * @param id
     * @return
     */
    def delete(id) {
        def blackList = BlackList.get(id)
        blackList.delete(flush: true, failOnError: true)
    }
}