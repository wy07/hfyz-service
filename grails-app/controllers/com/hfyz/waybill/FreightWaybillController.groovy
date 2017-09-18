package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.infoCenter.SourceType
import com.hfyz.security.User
import com.hfyz.support.SystemCode

class FreightWaybillController implements ControllerHelper {
    def freightWaybillService
    def infoCenterService
    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = freightWaybillService.search([vehicleNo  : request.JSON.vehicleNo
                                                   , ownerName: request.JSON.ownerName
                                                   , dateBegin: request.JSON.dateBegin
                                                   , status:    request.JSON.status
                                                   , dateEnd  : request.JSON.dateEnd], currentUser, max, offset)
        renderSuccessesWithMap(result)
    }

    def show() {
        withFreightWaybill(params.long('id')) { FreightWaybill freightWaybillInstance ->
            if (currentUser.isCompanyUser()) {
                if (freightWaybillInstance.companyCode != currentUser.companyCode) {
                    renderNoInstancePermError()
                    return
                }
            }
            if(request.JSON.action){
                if(request.JSON.action != freightWaybillInstance.status){
                    renderErrorMsg('此电子路单已被处理！')
                    return
                }
            }
            renderSuccessesWithMap([freightWaybill: [id                 : freightWaybillInstance.id
                                                     , vehicleNo        : freightWaybillInstance.vehicleNo
                                                     , frameNo          : freightWaybillInstance.frameNo
                                                     , licenseNo        : freightWaybillInstance.licenseNo
                                                     , carPlateColor    : freightWaybillInstance.carPlateColor
                                                     , carType          : freightWaybillInstance.carType
                                                     , carSize          : freightWaybillInstance.carSize
                                                     , companyCode      : freightWaybillInstance.companyCode
                                                     , ownerName        : freightWaybillInstance.ownerName
                                                     , dangerousName    : freightWaybillInstance.dangerousName
                                                     , dangerousType    : [id: freightWaybillInstance.dangerousType.id, name: freightWaybillInstance.dangerousType.name]
                                                     , ratifiedPayload  : freightWaybillInstance.ratifiedPayload
                                                     , emergencyPlan    : [id: freightWaybillInstance.emergencyPlan.id, name: freightWaybillInstance.emergencyPlan.name, describe: freightWaybillInstance.emergencyPlan.describe]
                                                     , price            : freightWaybillInstance.price
                                                     , operatedType     : freightWaybillInstance.operatedType
                                                     , loadedType       : freightWaybillInstance.loadedType
                                                     , fullLoaded       : freightWaybillInstance.fullLoaded
                                                     , amount           : freightWaybillInstance.amount
                                                     , mile             : freightWaybillInstance.mile
                                                     , departTime       : freightWaybillInstance.departTime?.format('yyyy-MM-dd HH:mm')
                                                     , driver           : [name: freightWaybillInstance.driverName, wokeLicenseNo: freightWaybillInstance.driverWokeLicenseNo, phone: freightWaybillInstance.driverPhone]
                                                     , supercargo       : [name: freightWaybillInstance.supercargoName, wokeLicenseNo: freightWaybillInstance.supercargoWokeLicenseNo, phone: freightWaybillInstance.supercargoPhone]
                                                     , consignCompany   : freightWaybillInstance.consignCompany
                                                     , backTime         : freightWaybillInstance.backTime?.format('yyyy-MM-dd  HH:mm')
                                                     , departArea       : freightWaybillInstance.departArea
                                                     , arriveArea       : freightWaybillInstance.arriveArea
                                                     , status           : freightWaybillInstance.status
                                                     , routerName       : freightWaybillInstance.routerName
                                                     , viaLand          : freightWaybillInstance.viaLand
                                                     , startProvince    : freightWaybillInstance.startProvince
                                                     , startCity        : freightWaybillInstance.startCity
                                                     , startDistrict    : freightWaybillInstance.startDistrict
                                                     , startProvinceCode: freightWaybillInstance.startProvinceCode
                                                     , startCityCode    : freightWaybillInstance.startCityCode
                                                     , startDistrictCode: freightWaybillInstance.startDistrictCode
                                                     , endProvince      : freightWaybillInstance.endProvince
                                                     , endCity          : freightWaybillInstance.endCity
                                                     , endDistrict      : freightWaybillInstance.endDistrict
                                                     , endProvinceCode  : freightWaybillInstance.endProvinceCode
                                                     , endCityCode      : freightWaybillInstance.endCityCode
                                                     , endDistrictCode  : freightWaybillInstance.endDistrictCode
            ]])
        }
    }

    def save() {
        if (!currentUser.isCompanyUser()) {
            renderNoInstancePermError()
            return
        }
        def dangerousType = SystemCode.get(request.JSON.dangerousType.id)
        FreightWaybill freightWaybillInstance = new FreightWaybill(request.JSON)
        freightWaybillInstance.status = 'CG'
        freightWaybillInstance.dangerousType = dangerousType
        freightWaybillInstance.driverName = request.JSON.driver.name
        freightWaybillInstance.driverWokeLicenseNo = request.JSON.driver.wokeLicenseNo
        freightWaybillInstance.driverPhone = request.JSON.driver.phone
        freightWaybillInstance.supercargoName = request.JSON.supercargo.name
        freightWaybillInstance.supercargoWokeLicenseNo = request.JSON.supercargo.wokeLicenseNo
        freightWaybillInstance.supercargoPhone = request.JSON.supercargo.phone
        freightWaybillInstance.companyCode = currentUser.companyCode
        freightWaybillInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withCompanyFreightWaybill(params.long('id'), currentUser) {FreightWaybill freightWaybillInstance ->
            renderSuccessesWithMap([freightWaybill: [id                 : freightWaybillInstance.id
                                                     , vehicleNo        : freightWaybillInstance.vehicleNo
                                                     , frameNo          : freightWaybillInstance.frameNo
                                                     , licenseNo        : freightWaybillInstance.licenseNo
                                                     , carPlateColor    : freightWaybillInstance.carPlateColor
                                                     , carType          : freightWaybillInstance.carType
                                                     , carSize          : freightWaybillInstance.carSize
                                                     , companyCode      : freightWaybillInstance.companyCode
                                                     , ownerName        : freightWaybillInstance.ownerName
                                                     , dangerousName    : freightWaybillInstance.dangerousName
                                                     , dangerousType    : [id: freightWaybillInstance.dangerousType.id, name: freightWaybillInstance.dangerousType.name]
                                                     , ratifiedPayload  : freightWaybillInstance.ratifiedPayload
                                                     , emergencyPlan    : [id: freightWaybillInstance.emergencyPlan.id, name: freightWaybillInstance.emergencyPlan.name, describe: freightWaybillInstance.emergencyPlan.describe]
                                                     , price            : freightWaybillInstance.price
                                                     , operatedType     : freightWaybillInstance.operatedType
                                                     , loadedType       : freightWaybillInstance.loadedType
                                                     , fullLoaded       : freightWaybillInstance.fullLoaded
                                                     , amount           : freightWaybillInstance.amount
                                                     , mile             : freightWaybillInstance.mile
                                                     , departTime       : freightWaybillInstance.departTime?.format('yyyy-MM-dd HH:mm')
                                                     , driver           : [name: freightWaybillInstance.driverName, wokeLicenseNo: freightWaybillInstance.driverWokeLicenseNo, phone: freightWaybillInstance.driverPhone]
                                                     , supercargo       : [name: freightWaybillInstance.supercargoName, wokeLicenseNo: freightWaybillInstance.supercargoWokeLicenseNo, phone: freightWaybillInstance.supercargoPhone]
                                                     , consignCompany   : freightWaybillInstance.consignCompany
                                                     , backTime         : freightWaybillInstance.backTime?.format('yyyy-MM-dd HH:mm')
                                                     , departArea       : freightWaybillInstance.departArea
                                                     , arriveArea       : freightWaybillInstance.arriveArea
                                                     , status           : freightWaybillInstance.status
                                                     , routerName       : freightWaybillInstance.routerName
                                                     , viaLand          : freightWaybillInstance.viaLand
                                                     , startProvince    : freightWaybillInstance.startProvince
                                                     , startCity        : freightWaybillInstance.startCity
                                                     , startDistrict    : freightWaybillInstance.startDistrict
                                                     , startProvinceCode: freightWaybillInstance.startProvinceCode
                                                     , startCityCode    : freightWaybillInstance.startCityCode
                                                     , startDistrictCode: freightWaybillInstance.startDistrictCode
                                                     , endProvince      : freightWaybillInstance.endProvince
                                                     , endCity          : freightWaybillInstance.endCity
                                                     , endDistrict      : freightWaybillInstance.endDistrict
                                                     , endProvinceCode  : freightWaybillInstance.endProvinceCode
                                                     , endCityCode      : freightWaybillInstance.endCityCode
                                                     , endDistrictCode  : freightWaybillInstance.endDistrictCode
            ]])
        }

    }

    def update() {
        withCompanyFreightWaybill(params.long('id'), currentUser) { FreightWaybill freightWaybillInstance ->
            def dangerousType = SystemCode.get(request.JSON.dangerousType.id)
            freightWaybillInstance.dangerousType = dangerousType
            freightWaybillInstance.departTime = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.departTime)
            freightWaybillInstance.backTime = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.backTime)
            freightWaybillInstance.driverName = request.JSON.driver.name
            freightWaybillInstance.driverWokeLicenseNo = request.JSON.driver.wokeLicenseNo
            freightWaybillInstance.driverPhone = request.JSON.driver.phone
            freightWaybillInstance.supercargoName = request.JSON.supercargo.name
            freightWaybillInstance.supercargoWokeLicenseNo = request.JSON.supercargo.wokeLicenseNo
            freightWaybillInstance.supercargoPhone = request.JSON.supercargo.phone
            request.JSON.remove('departTime')
            request.JSON.remove('backTime')
            freightWaybillInstance.properties = request.JSON
            freightWaybillInstance.save(flush: true, failOnError: true)
            renderSuccess()
        }
    }

    def delete() {
        withCompanyFreightWaybill(params.long('id'), currentUser) { FreightWaybill freightWaybillInstance ->
            freightWaybillInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def submit() {
        withCompanyFreightWaybill(params.long('id'), currentUser) { FreightWaybill freightWaybillInstance ->
            freightWaybillInstance.status = 'SHZ'
            freightWaybillInstance.save(flush: true, failOnError: true)
            infoCenterService.save(freightWaybillInstance.id, SourceType.DZLD)
            renderSuccess()
        }

    }

    def export() {
        def begin = request.JSON.dateBegin ? Date.parse('yyyy-MM-dd HH:mm:ss', request.JSON.dateBegin) : ''
        def end = request.JSON.dateEnd ? Date.parse('yyyy-MM-dd HH:mm:ss', request.JSON.dateEnd) : ''
        def freightWaybillList = FreightWaybill.createCriteria().list(){
            if (getCurrentUser().isCompanyUser()) {
                eq('companyCode', getCurrentUser().companyCode)
            } else if (request.JSON.ownerName) {
                like('ownerName', "${request.JSON.ownerName}%")
            }
            if (request.JSON.vehicleNo) {
                like('vehicleNo', "${request.JSON.vehicleNo}%")
            }
            if (begin && end) {
                between('departTime', begin, end)
            }
        }?.collect {FreightWaybill obj ->
            [
               车牌号: obj.vehicleNo,
               车架号: obj.frameNo,
               业户编码: obj.companyCode,
               业户名称: obj.ownerName,
               危险品名称: obj.dangerousName,
               危险品分类: obj.dangerousType?.name,
               状态: obj.status == 'CG'? '未审核':(obj.status == 'SHZ'?'审核中':(obj.status == 'YJJ'?'审核拒绝':'审核通过')),
               应急预案: obj.emergencyPlan?.name,
               车辆颜色: obj.carPlateColor,
               车辆类型: obj.carType,
               车辆尺寸: obj.carSize,
               挂车车牌号: obj.licenseNo,
               '核定载重质量(kg)': obj.ratifiedPayload,
               '运输价格(元/车)': obj.price,
               是否经营性运输: obj.operatedType,
               '装载/卸载': obj.loadedType,
               是否满载: obj.fullLoaded,
               装载量: obj.amount,
               运输距离: obj.mile,
               运输出场时间: obj.departTime?.format('yyyy-MM-dd HH:mm'),
               驾驶员姓名: obj.driverName,
               驾驶员从业资格证号: obj.driverWokeLicenseNo,
               驾驶员联系电话:  obj.driverPhone,
               押运员姓名: obj.supercargoName,
               押运员从业资格证号: obj.supercargoWokeLicenseNo,
               押运员联系电话: obj.supercargoPhone,
               托运单位: obj.consignCompany,
               托运回场时间: obj.backTime?.format('yyyy-MM-dd HH:mm'),
               出发地: obj.departArea,
               目的地: obj.arriveArea,
               路线名称: obj.routerName,
               起始地省市名称: obj.startProvince,
               起始地省市编码: obj.startProvinceCode,
               起始地城市名称: obj.startCity,
               起始地城市编码: obj.startCityCode,
               起始地区域名称: obj.startDistrict,
               起始地区域编码: obj.startDistrictCode,
               目的地省市名称: obj.endProvince,
               目的地省市编码: obj.endProvinceCode,
               目的地城市名称: obj.endCity,
               目的地城市编码: obj.endCityCode,
               目的地区域名称: obj.endDistrict,
               目的地区域编码: obj.endDistrictCode,
               途经地名称: obj.viaLand
            ]
        }
        renderSuccessesWithMap([freightWaybillList: freightWaybillList])
    }

    private withFreightWaybill(Long id, Closure c) {
        FreightWaybill freightWaybillInstance = id ? FreightWaybill.get(id) : null
        if (freightWaybillInstance) {
            c.call freightWaybillInstance
        } else {
            renderNoTFoundError()
        }
    }

    private withCompanyFreightWaybill(Long id, User user, Closure c) {
        FreightWaybill freightWaybillInstance = id ? FreightWaybill.get(id) : null
        if (freightWaybillInstance) {
            if (freightWaybillInstance.companyCode != user.companyCode) {
                renderNoInstancePermError()
            } else {
                c.call freightWaybillInstance
            }
        } else {
            renderNoTFoundError()
        }
    }
}
