package com.hfyz.roster

import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
import com.hfyz.car.CarBasicInfo
import grails.transaction.Transactional

@Transactional
class WhiteListService {

    WhiteList getWhiteList(Long id) {
        WhiteList whiteList = id ? WhiteList.get(id) : null
        if (!whiteList) {
            throw new RecordNotFoundException()
        }
        return whiteList
    }

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
    def more(Long id) {
        WhiteList instance = getWhiteList(id)
        CarBasicInfo vehicle = CarBasicInfo.findByLicenseNo(instance.vehicleNo)
        return [id           : instance.id,
                vehicleNo    : instance.vehicleNo,
                carType      : vehicle?.carType,
                carPlateColor: vehicle?.carPlateColor,
                carColor     : vehicle?.carColor]
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
