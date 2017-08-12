package com.hfyz.waybill

import com.hfyz.security.User
import grails.transaction.Transactional

@Transactional
class FreightRouterService {

    def getListAndTotal(max, offset,User user){

        def freightRouterList=FreightRouter.createCriteria().list([max: max, offset: offset, sort: 'id']) {
            if(user.isCompanyUser()){
                eq('companyCode',user.companyCode)
            }
        }?.collect { FreightRouter obj ->
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

        def total = FreightRouter.createCriteria().get {
            projections {
                count()
            }
            if(user.isCompanyUser()){
                eq('companyCode',user.companyCode)
            }
        }

        return [resultList: freightRouterList, total: total]
    }
}
