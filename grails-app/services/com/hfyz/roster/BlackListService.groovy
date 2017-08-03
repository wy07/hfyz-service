package com.hfyz.roster

import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
import com.hfyz.car.CarBasicInfo
import grails.transaction.Transactional

@Transactional
class BlackListService {
    BlackList getInstanceById(Long id) {
        BlackList instance = id ? BlackList.get(id) : null
        if (!instance) {
            throw new RecordNotFoundException()
        }
        return instance
    }

    /**
     * 列表查询
     * @param vehicleNo 车辆号牌
     * @param dateBegin 起始时间
     * @param dateEnd 结束时间
     * @param max
     * @param offset
     */
    def showTableList(vehicleNo, dateBegin, dateEnd, max, offset) {
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
    def show(id) {
        BlackList instance = getInstanceById(id)
        def vehicle = CarBasicInfo.findByLicenseNo(instance.vehicleNo)
        return [
                id           : instance.id,
                vehicleNo    : instance.vehicleNo,
                controlBegin : instance.controlBegin?.format("yyyy-MM-dd HH:mm:ss"),
                controlEnd   : instance.controlEnd?.format("yyyy-MM-dd HH:mm:ss"),
                carType      : vehicle?.carType,
                carPlateColor: vehicle?.carPlateColor,
                carColor     : vehicle?.carColor
        ]

    }

    /**
     * 更新
     * @param id
     * @param obj 请求json串
     */
    def update(Long id, obj) {
        BlackList instance = getInstanceById(id)
        instance.controlBegin = Date.parse("yyyy-MM-dd HH:mm:ss", obj.controlBegin)
        instance.controlEnd = Date.parse("yyyy-MM-dd HH:mm:ss", obj.controlEnd)
        instance.save(flush: true, failOnError: true)
    }

    /**
     * 保存黑名单信息，验证车牌号是否存在，如不存在，保存不成功
     * @param obj
     */
    def save(obj) {
        def carBasicInfo = CarBasicInfo.findByLicenseNo(obj.vehicleNo)
        if (!carBasicInfo) {
            throw new ParamsIllegalException("车辆号不存在！")
        }
        BlackList instance = new BlackList(obj)
        instance.frameNo = carBasicInfo.frameNo
        instance.save(flush: true, failOnError: true)
    }

    /**
     * 删除黑名单信息记录
     * @param id
     * @return
     */
    def delete(Long id) {
        BlackList instance = getInstanceById(id)
        instance.delete(flush: true, failOnError: true)
    }
}