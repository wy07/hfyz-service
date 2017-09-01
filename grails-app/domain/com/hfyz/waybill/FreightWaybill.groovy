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
    Date backTime //托运会回场时间
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
}
