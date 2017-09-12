package com.hfyz.waybill

class FreightRouter {
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
    String companyCode

    static constraints = {
        routerName nullable: false, blank: false, maxSize: 50
        startProvince nullable: false, blank: false, maxSize: 20
        startProvinceCode nullable: false, blank: false, maxSize: 20
        startCity nullable: false, blank: false, maxSize: 20
        startCityCode nullable: false, blank: false, maxSize: 20
        startDistrict nullable: false, blank: false, maxSize: 20
        startDistrictCode nullable: false, blank: false, maxSize: 20
        endProvince nullable: false, blank: false, maxSize: 20
        endProvinceCode nullable: false, blank: false, maxSize: 20
        endCity nullable: false, blank: false, maxSize: 20
        endCityCode nullable: false, blank: false, maxSize: 20
        endDistrict nullable: false, blank: false, maxSize: 20
        endDistrictCode nullable: false, blank: false, maxSize: 20
        viaLand nullable: true, blank: false, maxSize: 200
        companyCode nullable: false, blank: false
    }

    Object asType(Class clazz) {

        if (clazz == Map.class) {
            Map map = [id                 : this.id
                       , routerName       : this.routerName
                       , startProvince    : this.startProvince
                       , startCity        : this.startCity
                       , startDistrict    : this.startDistrict
                       , startProvinceCode: this.startProvinceCode
                       , startCityCode    : this.startCityCode
                       , startDistrictCode: this.startDistrictCode
                       , endProvince      : this.endProvince
                       , endCity          : this.endCity
                       , endDistrict      : this.endDistrict
                       , endProvinceCode  : this.endProvinceCode
                       , endCityCode      : this.endCityCode
                       , endDistrictCode  : this.endDistrictCode
                       , provenance       : this.startProvince + '/' + this.startCity + '/' + this.startDistrict
                       , destination      : this.endProvince + '/' + this.endCity + '/' + this.endDistrict
                       , viaLand          : this.viaLand]
            return map
        }
        return null
    }
}
