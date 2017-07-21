package com.hfyz.people

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
    def getPeopleList(name, phoneNo, IDCardNo, max, offset) {
        def total = PeopleBasicInfo.count()
        def resultList = PeopleBasicInfo.createCriteria().list(max: max, offset: offset) {
            if (name) {
                like("name", "%${name}%")
            }
            if (phoneNo) {
                like("phoneNo", "${phoneNo}")
            }
            if (IDCardNo) {
                like("IDCardNo", "${IDCardNo}")
            }
        }?.collect({ PeopleBasicInfo info ->
            [
                    name           : info.name,
                    gender         : info.gender,
                    IDCardNo       : info.IDCardNo,
                    birthday       : info.birthday?.format("yyyy-MM-dd"),
                    nation         : info.nation,
                    nativePlace    : info.nativePlace,
                    technologyTitle: info.technologyTitle,
                    phoneNo        : info.phoneNo,

            ]
        })

        println(resultList.size())

        return [resultList: resultList, total: total]
    }

    /**
     * 查看详情
     * @param IDCardNo
     */
    def getDetailInfo(IDCardNo) {
        def result = [:]
        def checkMember = WorkerCheckMember.createCriteria().get {
            eq("IDCardNo", IDCardNo)
        }
        def coach = WorkerCoach.createCriteria().get {
            eq("IDCardNo", IDCardNo)
        }
        def driver = WorkerDriver.createCriteria().get {
            eq("IDCardNo", IDCardNo)
        }
        def manager = WorkerManager.createCriteria().get {
            eq("IDCardNo", IDCardNo)
        }
        def tech = WorkerTechnology.createCriteria().get {
            eq("IDCardNo", IDCardNo)
        }
        def waiter = WorkerWaiter.createCriteria().get {
            eq("IDCardNo", IDCardNo)
        }

        result["checkMember"] = checkMember
        result["coach"] = coach
        result["driver"] = driver
        result["manager"] = manager
        result["tech"] = tech
        result["waiter"] = waiter

        return result
    }
}
