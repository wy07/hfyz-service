package com.hfyz.waybill


class FreightStationService {

    def getListAndTotal(max, offset, user) {

        def freightStationList=FreightStation.createCriteria().list([max: max, offset: offset, sort: 'id']) {
            if(user.isCompanyUser()){
                eq('orgCode',user.companyCode)
            }
        }?.collect { FreightStation obj ->
            [ id: obj.id
              ,orgCode: obj.orgCode
              ,name: obj.name
              ,sn: obj.sn
              ,manageStatus: obj.manageStatus?.name
              ,approvalNumber: obj.approvalNumber
              ,districtName: obj.districtName
              ,districtCode: obj.districtCode
              ,level: obj.level?.name
              ,address: obj.address
            ]
        }

        def total = FreightStation.createCriteria().get {
            projections {
                count()
            }
            if(user.isCompanyUser()){
                eq('orgCode',user.companyCode)
            }
        }

        return [freightStationList: freightStationList, total: total]
    }

}
