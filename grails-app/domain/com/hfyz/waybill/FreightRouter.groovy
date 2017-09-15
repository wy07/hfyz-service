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

    static mapping = {
        comment '危货路线表'
        id generator: 'native', params: [sequence: 'freight_router_seq'], defaultValue: "nextval('freight_router_seq')"
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
        companyCode comment:'业户编码'
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
