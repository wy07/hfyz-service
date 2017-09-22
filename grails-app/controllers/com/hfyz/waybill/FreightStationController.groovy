package com.hfyz.waybill

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import grails.converters.JSON

class FreightStationController implements ControllerHelper {
    def fileManager
    def freightStationService

    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(freightStationService.getListAndTotal(max, offset, currentUser))
    }

    def save() {
        if (!currentUser.isCompanyUser()) {
            renderNoInstancePermError()
            return
        }
        def frontPhoto = request.getFile('frontPhoto')
        def sidePhoto = request.getFile('sidePhoto')
        def json = JSON.parse(params.freightStation)

        FreightStation freightStation = new FreightStation(json)
        freightStation.orgCode = getCurrentUser().companyCode
        freightStation.buildDate = new Date().parse('yyyy-MM-dd HH:mm',  json.build)
        freightStation.checkDate = new Date().parse('yyyy-MM-dd HH:mm',  json.check)
        freightStation.completedDate = new Date().parse('yyyy-MM-dd HH:mm',  json.completed)
        freightStation.operateDate = new Date().parse('yyyy-MM-dd HH:mm',  json.operate)

        freightStation.frontPhoto = fileManager.getFileRealPath(frontPhoto, 'freightStationPhoto', getCurrentUser().companyCode)
        freightStation.sidePhoto = fileManager.getFileRealPath(sidePhoto, 'freightStationPhoto', getCurrentUser().companyCode)
        fileManager.saveFile(frontPhoto, 'freightStationPhoto', getCurrentUser().companyCode)
        fileManager.saveFile(sidePhoto, 'freightStationPhoto', getCurrentUser().companyCode)
        freightStation.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        withFreightStation(params.long('id')) {FreightStation obj ->
            renderSuccessesWithMap([freightStation: [id: obj.id
                                                     ,orgCode: obj.orgCode
                                                     ,name: obj.name
                                                     ,cn: obj.cn
                                                     ,manageRange: obj.manageRange.collect{ it.id }
                                                     ,manageRangeList: obj.manageRange.collect{ it.name }
                                                     ,manageStatus: obj.manageStatus?.id
                                                     ,manageStatusName: obj.manageStatus?.name
                                                     ,buildDate: obj.buildDate?.format("yyyy-MM-dd HH:mm")
                                                     ,completedDate: obj.completedDate?.format("yyyy-MM-dd HH:mm")
                                                     ,checkDate: obj.checkDate?.format("yyyy-MM-dd HH:mm")
                                                     ,operateDate: obj.operateDate?.format("yyyy-MM-dd HH:mm")
                                                     ,scale: obj.scale
                                                     ,approvalNumber: obj.approvalNumber
                                                     ,districtName: obj.districtName
                                                     ,districtCode: obj.districtCode
                                                     ,level: obj.level?.id
                                                     ,levelName: obj.level?.name
                                                     ,address: obj.address
                                                     ,coverArea: obj.coverArea
                                                     ,buildArea: obj.buildArea
                                                     ,height: obj.height
            ]])
        }
    }

    def update() {
        if (!currentUser.isCompanyUser()) {
            renderNoInstancePermError()
            return
        }
        withFreightStation(params.long('id')) { FreightStation freightStation ->

            def frontPhoto = request.getFile('frontPhoto')
            def sidePhoto = request.getFile('sidePhoto')
            def json = JSON.parse(params.freightStation)
            json.remove('buildDate')
            json.remove('checkDate')
            json.remove('completedDate')
            json.remove('operateDate')

            freightStation.properties = json
            if(frontPhoto) {
                fileManager.deleteFile(freightStation.frontPhoto)
                freightStation.frontPhoto = fileManager.getFileRealPath(frontPhoto, 'freightStationPhoto', getCurrentUser().companyCode)
                fileManager.saveFile(frontPhoto, 'freightStationPhoto', getCurrentUser().companyCode)
            }
            if(sidePhoto) {
                fileManager.deleteFile(freightStation.sidePhoto)
                freightStation.sidePhoto = fileManager.getFileRealPath(sidePhoto, 'freightStationPhoto', getCurrentUser().companyCode)
                fileManager.saveFile(sidePhoto, 'freightStationPhoto', getCurrentUser().companyCode)
            }
            freightStation.orgCode = getCurrentUser().companyCode
            freightStation.buildDate = new Date().parse('yyyy-MM-dd HH:mm',  json.build)
            freightStation.checkDate = new Date().parse('yyyy-MM-dd HH:mm',  json.check)
            freightStation.completedDate = new Date().parse('yyyy-MM-dd HH:mm',  json.completed)
            freightStation.operateDate = new Date().parse('yyyy-MM-dd HH:mm',  json.operate)
            freightStation.save(flush: true, failOnError: true)
            renderSuccess()
        }
    }

    def delete() {
        if (!currentUser.isCompanyUser()) {
            renderNoInstancePermError()
            return
        }
        withFreightStation(params.long('id')) { FreightStation freightStation ->
            fileManager.deleteFile(freightStation.frontPhoto)
            fileManager.deleteFile(freightStation.sidePhoto)
            freightStation.delete(flush: true)
            renderSuccess()
        }
    }

    private withFreightStation(Long id, Closure c) {
        FreightStation freightStation = id ? FreightStation.get(id) : null
        if (freightStation) {
            c.call freightStation
        } else {
            renderNoTFoundError()
        }
    }
}
