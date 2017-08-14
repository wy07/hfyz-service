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
        routerName nullable: false, blank: false, maxSize:50
        startProvince nullable: false, blank: false, maxSize:20
        startProvinceCode nullable: false,blank: false,maxSize: 20
        startCity nullable: false,blank: false,maxSize: 20
        startCityCode nullable: false,blank: false,maxSize: 20
        startDistrict nullable: false,blank: false,maxSize: 20
        startDistrictCode nullable: false,blank: false,maxSize: 20
        endProvince nullable: false,blank: false,maxSize: 20
        endProvinceCode nullable: false,blank: false,maxSize: 20
        endCity nullable: false,blank: false,maxSize: 20
        endCityCode nullable: false,blank: false,maxSize: 20
        endDistrict nullable: false,blank: false,maxSize: 20
        endDistrictCode nullable: false,blank: false,maxSize: 20
        viaLand nullable: true,blank: false,maxSize: 200
        companyCode nullable: false,blank: false
    }
}
