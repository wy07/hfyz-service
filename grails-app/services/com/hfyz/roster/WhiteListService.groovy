package com.hfyz.roster

import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
import com.hfyz.car.CarBasicInfo
import grails.transaction.Transactional

@Transactional
class WhiteListService {

    WhiteList getInstanceById(Long id) {
        WhiteList instance = id ? WhiteList.get(id) : null
        if (!instance) {
            throw new RecordNotFoundException()
        }
        return instance
    }

    /**
     * 列表查询
     * @param vehicleNo 车辆号牌
     * @param max
     * @param offset
     */
    def showTableList(vehicleNo, max, offset) {
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
    def show(Long id) {
        WhiteList instance = getInstanceById(id)
        CarBasicInfo vehicle = CarBasicInfo.findByFrameNo(instance.frameNo)
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
        WhiteList instance = getInstanceById(id)
        /** *************/
        instance.save(flush: true, failOnError: true)
    }

    /**
     * 保存黑名单信息，验证车牌号是否存在，如不存在，保存不成功
     * @param obj
     */
    def save(obj) {
        CarBasicInfo carBasicInfo = CarBasicInfo.findByLicenseNo(obj.vehicleNo)
        if (!carBasicInfo) {
            throw new ParamsIllegalException("车牌号有误！")
        }
        WhiteList instance = new WhiteList(obj)
        instance.frameNo = carBasicInfo.frameNo
        instance.save(flush: true, failOnError: true)
    }

    /**
     * 删除黑名单信息记录
     * @param id
     * @return
     */
    def delete(Long id) {
        WhiteList instance = getInstanceById(id)
        instance.delete(flush: true, failOnError: true)
    }
}
