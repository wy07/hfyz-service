package com.hfyz.waybill


class FreightStationService {
    def fileManager
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

    def saveFreightStation(json, user, frontPhoto, sidePhoto) {
        FreightStation freightStation = new FreightStation(json)
        freightStation.orgCode = user.companyCode
        freightStation.buildDate = new Date().parse('yyyy-MM-dd HH:mm',  json.build)
        freightStation.checkDate = new Date().parse('yyyy-MM-dd HH:mm',  json.check)
        freightStation.completedDate = new Date().parse('yyyy-MM-dd HH:mm',  json.completed)
        freightStation.operateDate = new Date().parse('yyyy-MM-dd HH:mm',  json.operate)

        freightStation.frontPhoto = fileManager.getFileRealPath(frontPhoto, 'freightStationPhoto', user.companyCode)
        fileManager.saveFile(frontPhoto, 'freightStationPhoto', user.companyCode)
        freightStation.sidePhoto = fileManager.getFileRealPath(sidePhoto, 'freightStationPhoto', user.companyCode)
        fileManager.saveFile(sidePhoto, 'freightStationPhoto', user.companyCode)
        freightStation.save(flush: true, failOnError: true)


    }

    def updateFreightStation(freightStation, json, user, frontPhoto, sidePhoto) {
        freightStation.properties = json
        freightStation.orgCode = user.companyCode
        freightStation.buildDate = new Date().parse('yyyy-MM-dd HH:mm',  json.build)
        freightStation.checkDate = new Date().parse('yyyy-MM-dd HH:mm',  json.check)
        freightStation.completedDate = new Date().parse('yyyy-MM-dd HH:mm',  json.completed)
        freightStation.operateDate = new Date().parse('yyyy-MM-dd HH:mm',  json.operate)

        if(frontPhoto) {
            fileManager.deleteFile(freightStation.frontPhoto)
            freightStation.frontPhoto = fileManager.getFileRealPath(frontPhoto, 'freightStationPhoto', user.companyCode)
            fileManager.saveFile(frontPhoto, 'freightStationPhoto', user.companyCode)
        }
        if(sidePhoto) {
            fileManager.deleteFile(freightStation.sidePhoto)
            freightStation.sidePhoto = fileManager.getFileRealPath(sidePhoto, 'freightStationPhoto', user.companyCode)
            fileManager.saveFile(sidePhoto, 'freightStationPhoto', user.companyCode)
        }
        freightStation.save(flush: true, failOnError: true)
    }

}
