package com.hfyz.roster

import com.hfyz.car.CarBasicInfo
import grails.transaction.Transactional

@Transactional
class WhiteListService {

    /**
     * 列表查询
     * @param vehicleNo 车辆号牌
     * @param max
     * @param offset
     */
    def list(vehicleNo, max, offset) {
        def total = WhiteList.createCriteria().get {
            projections {
                count()
            }
            if (vehicleNo) {
                eq("vehicleNo", "${vehicleNo}")
            }
        }

        def resultList = WhiteList.createCriteria().list(max: max, offset: offset) {
            if (vehicleNo) {
                eq("vehicleNo", "${vehicleNo}")
            }
        }?.collect { WhiteList whiteList ->
            [
                    id       : whiteList.id,
                    vehicleNo: whiteList.vehicleNo
            ]
        }
        return [resultList: resultList, total: total]
    }

    /**
     * 查看详情
     */
    def more(id) {
        def instance = id ? WhiteList.get(id) : null
        def vehicle = CarBasicInfo.findByLicenseNo(instance.vehicleNo)
        if (instance && vehicle) {
            def result = [
                    id           : instance.id,
                    vehicleNo    : instance.vehicleNo,
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
        def whiteList = id ? WhiteList.get(id) : null
        if (!whiteList) {
            return false
        }
        whiteList.save(flush: true, failOnError: true)
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
        WhiteList whiteList = new WhiteList(obj)
        whiteList.save(flush: true, failOnError: true)
        return
    }

    /**
     * 删除黑名单信息记录
     * @param id
     * @return
     */
    def delete(id) {
        def whiteList = WhiteList.get(id)
        whiteList.delete(flush: true, failOnError: true)
    }
}
