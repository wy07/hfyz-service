package com.hfyz.waybill

import grails.transaction.Transactional

@Transactional
class FreightRouterService {

    def getListAndTotal(max, offset){
        def freightRouterList = FreightRouter.list([max: max, offset: offset, sort: 'id'])?.collect { FreightRouter obj ->
            [ id: obj.id
              ,routerName: obj.routerName
              ,startProvince: obj.startProvince
              ,startCity: obj.startCity
              ,startDistrict: obj.startDistrict
              ,startProvinceCode: obj.startProvinceCode
              ,startCityCode: obj.startCityCode
              ,startDistrictCode: obj.startDistrictCode
              ,endProvince: obj.endProvince
              ,endCity: obj.endCity
              ,endDistrict: obj.endDistrict
              ,endProvinceCode: obj.endProvinceCode
              ,endCityCode: obj.endCityCode
              ,endDistrictCode: obj.endDistrictCode
              ,provenance: obj.startProvince+'/'+obj.startCity+'/'+obj.startDistrict
              ,provenanceCode: obj.startProvinceCode+'/'+obj.startCityCode+'/'+obj.startDistrictCode
              ,destination: obj.endProvince+'/'+obj.endCity+'/'+obj.endDistrict
              ,destinationCode: obj.endProvinceCode+'/'+obj.endCityCode+'/'+obj.endDistrictCode
              ,viaLand:obj.viaLand
            ]
        }
        return [resultList: freightRouterList, total: FreightRouter.count()]
    }
}
