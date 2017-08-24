package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.security.User
import com.hfyz.support.SystemCode

class FreightWaybillController implements ControllerHelper {
    def freightWaybillService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = freightWaybillService.search([vehicleNo  : request.JSON.vehicleNo
                                                   , ownerName: request.JSON.ownerName
                                                   , dateBegin: request.JSON.dateBegin
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
                                                     , emergencyPlan    : freightWaybillInstance.emergencyPlan
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
        freightWaybillInstance.driverWokeLicenseNo = request.JSON.driver.workLicenseNo
        freightWaybillInstance.driverPhone = request.JSON.driver.phoneNo
        freightWaybillInstance.supercargoName = request.JSON.supercargo.name
        freightWaybillInstance.supercargoWokeLicenseNo = request.JSON.supercargo.workLicenseNo
        freightWaybillInstance.supercargoPhone = request.JSON.supercargo.phoneNo
        freightWaybillInstance.companyCode = currentUser.companyCode
        freightWaybillInstance.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withCompanyFreightWaybill(params.long('id'), currentUser) { freightWaybillInstance ->
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
                                                     , emergencyPlan    : freightWaybillInstance.emergencyPlan
                                                     , price            : freightWaybillInstance.price
                                                     , operatedType     : freightWaybillInstance.operatedType
                                                     , loadedType       : freightWaybillInstance.loadedType
                                                     , fullLoaded       : freightWaybillInstance.fullLoaded
                                                     , amount           : freightWaybillInstance.amount
                                                     , mile             : freightWaybillInstance.mile
                                                     , departTime       : freightWaybillInstance.departTime?.format('yyyy-MM-dd HH:mm')
                                                     , driver           : [name: freightWaybillInstance.driverName, workLicenseNo: freightWaybillInstance.driverWokeLicenseNo, phoneNo: freightWaybillInstance.driverPhone]
                                                     , supercargo       : [name: freightWaybillInstance.supercargoName, workLicenseNo: freightWaybillInstance.supercargoWokeLicenseNo, phoneNo: freightWaybillInstance.supercargoPhone]
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
            freightWaybillInstance.driverWokeLicenseNo = request.JSON.driver.workLicenseNo
            freightWaybillInstance.driverPhone = request.JSON.driver.phoneNo
            freightWaybillInstance.supercargoName = request.JSON.supercargo.name
            freightWaybillInstance.supercargoWokeLicenseNo = request.JSON.supercargo.workLicenseNo
            freightWaybillInstance.supercargoPhone = request.JSON.supercargo.phoneNo
            request.JSON.remove('departTime')
            request.JSON.remove('backTime')
            freightWaybillInstance.properties = request.JSON
            freightWaybillInstance.save(flush: true, failOnError: true)
            renderSuccess()
        }
    }

    def delete() {
        withCompanyFreightWaybill(params.long('id'), currentUser) { freightWaybillInstance ->
            freightWaybillInstance.delete(flush: true)
            renderSuccess()
        }
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
