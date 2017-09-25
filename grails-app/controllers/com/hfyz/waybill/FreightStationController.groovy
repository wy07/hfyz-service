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
        freightStationService.saveFreightStation(json, getCurrentUser(), frontPhoto, sidePhoto)
        renderSuccess()
    }

    def edit() {
        withFreightStation(params.long('id')) {FreightStation obj ->

            File frontPhoto = new File(obj.frontPhoto)
            def frontPhotobase64String = getImgBase64Code(frontPhoto)
            File sidePhoto = new File(obj.sidePhoto)
            def sidePhotobase64String = getImgBase64Code(sidePhoto)
            renderSuccessesWithMap([freightStation: [id: obj.id
                                                     ,orgCode: obj.orgCode
                                                     ,name: obj.name
                                                     ,sn: obj.sn
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
                                                     ,frontPhotobase64String: frontPhotobase64String
                                                     ,sidePhotobase64String: sidePhotobase64String
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

            freightStationService.updateFreightStation(freightStation, json, getCurrentUser(), frontPhoto, sidePhoto)
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

    private getImgBase64Code(file) {

        String base64String = null
        FileInputStream fileInputStreamReader
        try {
            fileInputStreamReader = new FileInputStream(file)
            byte[] bytes = new byte[(int)file.length()]
            fileInputStreamReader.read(bytes)
            base64String = Base64.getEncoder().encodeToString(bytes)
            fileInputStreamReader.close()
        } catch (FileNotFoundException e) {
            renderNoTFoundError()
        } catch (IOException e) {
            e.printStackTrace()
        }finally {
            if (fileInputStreamReader != null) {
                fileInputStreamReader.close()
            }
        }
        return base64String
    }
}
