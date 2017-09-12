package com.hfyz.waybill

import com.hfyz.support.DangerousType

/**
 * 货运电子路单
 */
class FreightWaybill {
    String vehicleNo // 车牌号
    String frameNo // 车架号
    String carPlateColor // 车辆颜色
    String carType // 车辆类型
    String carSize // 车辆尺寸
    String licenseNo // 挂车车牌号
    String companyCode // 业户编码
    String ownerName //业户名称
    String dangerousName //危险品名称
    DangerousType dangerousType //危险品分类
    EmergencyPlan emergencyPlan //应急预案
    Double ratifiedPayload //核定载重质量,kg
//    String emergencyPlan //应急预案
    Double price //运输价格 元/车
    String operatedType //是否经营性运输
    String loadedType // 装载or卸载
    String fullLoaded //是否满载
    Double amount // 装载量
    Double mile //运输距离
    Date departTime //运输出场时间
    String driverName //驾驶员姓名
    String driverWokeLicenseNo //驾驶员从业资格证号
    String driverPhone //驾驶员联系电话
    String supercargoName //押运员姓名
    String supercargoWokeLicenseNo // 押运员从业资格证号
    String supercargoPhone //押运员联系电话
    String consignCompany //托运单位
    Date backTime //托运回场时间
    String departArea //出发地
    String arriveArea //目的地
    String status //状态
    String routerName
    String startProvince
    String startProvinceCode
    String startCity
    String startCityCode
    String startDistrict
    String startDistrictCode
    String endProvince
    String endProvinceCode
    String endCity
    String endCityCode
    String endDistrict
    String endDistrictCode
    String viaLand

    static constraints = {

    }

    static mapping = {
        comment '货运电子路单表'
        id generator: 'native', params: [sequence: 'freight_waybill_seq'], defaultValue: "nextval('freight_waybill_seq')"
        vehicleNo comment:'车牌号'
        frameNo comment:'描述'
        carPlateColor comment:'车辆颜色'
        carType comment:'车辆类型'
        carSize comment:'车辆尺寸'
        licenseNo comment:'挂车车牌号'
        companyCode comment:'业户编码'
        ownerName comment:'业户名称'
        dangerousName comment:'危险品名称'
        dangerousType comment:'危险品分类'
        emergencyPlan comment:'应急预案'
        ratifiedPayload comment:'核定载重质量,kg'
        price comment:'运输价格 元/车'
        operatedType comment:'是否经营性运输'
        loadedType comment:'装载or卸载'
        fullLoaded comment:'是否满载'
        amount comment:'装载量'
        mile comment:'运输距离'
        departTime comment:'运输出场时间'
        driverName comment:'驾驶员姓名'
        driverWokeLicenseNo comment:'驾驶员从业资格证号'
        driverPhone comment:'驾驶员联系电话'
        supercargoName comment:'押运员姓名'
        supercargoWokeLicenseNo comment:'押运员从业资格证号'
        supercargoPhone comment:'押运员联系电话'
        consignCompany comment:'托运单位'
        backTime comment:'托运回场时间'
        departArea comment:'出发地'
        arriveArea comment:'目的地'
        status comment:'状态'
        routerName comment:'路线名称'
        startProvince comment:'起始地省市名称'
        startProvinceCode comment:'起始地省市编码'
        startCity comment:'起始地城市名称'
        startCityCode comment:'起始地城市编码'
        startDistrict comment:'起始地区域名称'
        startDistrictCode comment:'起始地区域编码'
        endProvince comment:'目的地省市名称'
        endProvinceCode comment:'目的地省市编码'
        endCity comment:'目的地城市名称'
        endCityCode comment:'目的地城市编码'
        endDistrict comment:'目的地区域名称'
        endDistrictCode comment:'目的地区域编码'
        viaLand comment:'途经地名称'
    }
}
