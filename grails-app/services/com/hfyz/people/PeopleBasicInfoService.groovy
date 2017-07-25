package com.hfyz.people

import com.sun.xml.internal.bind.v2.model.core.ID
import grails.transaction.Transactional

@Transactional
class PeopleBasicInfoService {

    /**
     * 列表查询
     * @param name 姓名
     * @param phoneNo 手机号
     * @param IDCardNo 身份证号
     * @param max
     * @param offset
     * @return
     */
    def getPeopleList(name, phoneNo, idCardNo, max, offset) {
        def total = PeopleBasicInfo.createCriteria().get {
            projections {
                count()
            }
            if (name) {
                like("name", "${name}")
            }
            if (phoneNo) {
                like("phoneNo", "${phoneNo}")
            }
            if (idCardNo) {
                like("idCardNo", "${idCardNo}")
            }
        }
        def resultList = PeopleBasicInfo.createCriteria().list(max: max, offset: offset) {
            if (name) {
                like("name", "${name}")
            }
            if (phoneNo) {
                like("phoneNo", "${phoneNo}")
            }
            if (idCardNo) {
                like("idCardNo", "${idCardNo}")
            }
        }?.collect({ PeopleBasicInfo info ->
            [
                    name           : info.name,
                    gender         : info.gender,
                    IDCardNo       : info.idCardNo,
                    birthday       : info.birthday?.format("yyyy-MM-dd"),
                    nation         : info.nation,
                    nativePlace    : info.nativePlace,
                    technologyTitle: info.technologyTitle,
                    phoneNo        : info.phoneNo,
            ]
        })

        return [resultList: resultList, total: total]
    }

    /**
     * 查看详情
     * @param idCardNo
     */
    def getDetailInfo(idCardNo) {
        def result = [:]

        def checkMember = WorkerCheckMember.findByIdCardNo(idCardNo)
        checkMember?.workLicenseGrantTime?.format("yyyy-MM-dd HH:mm:ss")
        checkMember?.workLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")
        checkMember?.endTime?.format("yyyy-MM-dd HH:mm:ss")

        def coach = WorkerCoach.findByIdCardNo(idCardNo)
        coach?.workLicenseGrantTime?.format("yyyy-MM-dd HH:mm:ss")
        coach?.workLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")
        coach?.endTime?.format("yyyy-MM-dd HH:mm:ss")
        coach?.driveLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")

        def driver = WorkerDriver.findByIdCardNo(idCardNo)
        driver?.workLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")
        driver?.workLicenseGrantTime?.format("yyyy-MM-dd HH:mm:ss")
        driver?.endTime?.format("yyyy-MM-dd HH:mm:ss")
        driver?.driveLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")

        def manager = WorkerManager.findByIdCardNo(idCardNo)
        manager?.workLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")
        manager?.workLicenseGrantTime?.format("yyyy-MM-dd HH:mm:ss")
        manager?.endTime?.format("yyyy-MM-dd HH:mm:ss")

        def tech = WorkerTechnology.findByIdCardNo(idCardNo)
        tech?.workLicenseGetTime?.format("yyyy-MM-dd HH:mm:ss")
        tech?.workLicenseGrantTime?.format("yyyy-MM-dd HH:mm:ss")
        tech?.endTime?.format("yyyy-MM-dd HH:mm:ss")
        tech?.technologyLicenseGrantTime?.format("yyyy-MM-dd HH:mm:ss")

        def waiter = WorkerWaiter.findByIdCardNo(idCardNo)
        waiter?.beginWorkTime?.format("yyyy-MM-dd HH:mm:ss")
        waiter?.grantTime?.format("yyyy-MM-dd HH:mm:ss")

        result["checkMember"] = checkMember
        result["coach"] = coach
        result["driver"] = driver
        result["manager"] = manager
        result["tech"] = tech
        result["waiter"] = waiter

        return result
    }

    /**
     * 从业人员资格巡检
     */
    def licenseInspection() {
        //获取当前时间
        def currentTime = new Date()

        
    }

}
