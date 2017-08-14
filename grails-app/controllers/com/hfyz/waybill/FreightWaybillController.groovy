package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.hfyz.security.User
import com.hfyz.support.DangerousType

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
        withCompanyFreightWaybill(params.long('id'), currentUser) { FreightWaybill freightWaybillInstance ->
            renderSuccessesWithMap([freightWaybill: [id                 : freightWaybillInstance.id
                                                     , vehicleNo        : freightWaybillInstance.vehicleNo
                                                     , frameNo          : freightWaybillInstance.frameNo
                                                     , companyCode      : freightWaybillInstance.companyCode
                                                     , ownerName        : freightWaybillInstance.ownerName
                                                     , dangerousName    : freightWaybillInstance.dangerousName
                                                     , dangerousType    : freightWaybillInstance.dangerousType.name
                                                     , ratifiedPayload  : freightWaybillInstance.ratifiedPayload
                                                     , emergencyPlan    : freightWaybillInstance.emergencyPlan
                                                     , price            : freightWaybillInstance.price
                                                     , operatedType     : freightWaybillInstance.operatedType
                                                     , loadedType       : freightWaybillInstance.loadedType
                                                     , fullLoaded       : freightWaybillInstance.fullLoaded
                                                     , amount           : freightWaybillInstance.amount
                                                     , mile             : freightWaybillInstance.mile
                                                     , departTime       : freightWaybillInstance.departTime?.format('yyyy-MM-dd')
                                                     , driverName       : freightWaybillInstance.driverName
                                                     , idCardNo         : freightWaybillInstance.idCardNo
                                                     , consignCompany   : freightWaybillInstance.consignCompany
                                                     , backTime         : freightWaybillInstance.backTime?.format('yyyy-MM-dd')
                                                     , departArea       : freightWaybillInstance.departArea
                                                     , arriveArea       : freightWaybillInstance.arriveArea
                                                     , status           : freightWaybillInstance.status
                                                     , routerName       : freightWaybillInstance.routerName
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
                                                     , provenance       : freightWaybillInstance.startProvince + '/' + freightWaybillInstance.startCity + '/' + freightWaybillInstance.startDistrict
                                                     , destination      : freightWaybillInstance.endProvince + '/' + freightWaybillInstance.endCity + '/' + freightWaybillInstance.endDistrict
            ]])
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
