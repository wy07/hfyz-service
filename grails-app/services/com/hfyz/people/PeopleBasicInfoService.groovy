package com.hfyz.people

import com.commons.utils.SQLHelper
import com.hfyz.support.AlarmType
import com.hfyz.warning.Alarm
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.SourceType
import com.hfyz.workOrder.WorkOrder
import grails.transaction.Transactional

@Transactional
class PeopleBasicInfoService {
    def dataSource

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
        checkMember?.workLicenseGrantTime?.format("yyyy-MM-dd")
        checkMember?.workLicenseGetTime?.format("yyyy-MM-dd")
        checkMember?.endTime?.format("yyyy-MM-dd")

        def coach = WorkerCoach.findByIdCardNo(idCardNo)
        coach?.workLicenseGrantTime?.format("yyyy-MM-dd")
        coach?.workLicenseGetTime?.format("yyyy-MM-dd")
        coach?.endTime?.format("yyyy-MM-dd")
        coach?.driveLicenseGetTime?.format("yyyy-MM-dd")

        def driver = WorkerDriver.findByIdCardNo(idCardNo)
        driver?.workLicenseGetTime?.format("yyyy-MM-dd")
        driver?.workLicenseGrantTime?.format("yyyy-MM-dd")
        driver?.endTime?.format("yyyy-MM-dd")
        driver?.driveLicenseGetTime?.format("yyyy-MM-dd")

        def manager = WorkerManager.findByIdCardNo(idCardNo)
        manager?.workLicenseGetTime?.format("yyyy-MM-dd")
        manager?.workLicenseGrantTime?.format("yyyy-MM-dd")
        manager?.endTime?.format("yyyy-MM-dd")

        def tech = WorkerTechnology.findByIdCardNo(idCardNo)
        tech?.workLicenseGetTime?.format("yyyy-MM-dd")
        tech?.workLicenseGrantTime?.format("yyyy-MM-dd")
        tech?.endTime?.format("yyyy-MM-dd")
        tech?.technologyLicenseGrantTime?.format("yyyy-MM-dd")

        def waiter = WorkerWaiter.findByIdCardNo(idCardNo)
        waiter?.beginWorkTime?.format("yyyy-MM-dd")
        waiter?.grantTime?.format("yyyy-MM-dd")

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
        this.endTimeInspection()
        this.illegalInspection()
    }

    /**
     * 营运资格到期检查
     * @return
     */
    private endTimeInspection() {
        //根据人员信息分类和表名创建对应map
        def data = [
                people_worker_checkmember: "考核员",
                people_worker_coach      : "教练员",
                people_worker_driver     : "营运驾驶员",
                people_worker_manager    : "危货押运/装卸管理员",
                people_worker_technology : "维修技术人员"
//                people_worker_waiter     : "站场服务人员"
        ]

        //获取时间 查询参数
        def current = new Date()
        def params = [range: (current + 30).format("yyyy-MM-dd")]

        def resultList = []

        data.each { e ->
            //分别从各个人员类别表中查询资格到期信息
            def list = SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getSql(e.key), params)
            }?.collect({ obj ->
                [
                        idCardNo   : obj.idCardNo,
                        companyCode: obj.companyCode,
                        endTime    : obj.endTime,
                        type       : e.value
                ]
            })
            resultList += list
        }

        AlarmType typeDefault = AlarmType.findByCodeNum("209")//到期警告
        AlarmType typeOverdue = AlarmType.findByCodeNum("210")// 过期警告
        def level = AlarmLevel.PROMPT

        resultList.each { e ->
            def note = "${e.type}资格证还有 ${e.endTime - current} 天到期"
            //如果资格到期时间小于当前时间，则为过期警告
            if (e.endTime < current) {
                typeDefault = typeOverdue
                level = AlarmLevel.SERIOUS
                note = "${e.type}资格证已过期 ${current - e.endTime} 天"
            }

            new Alarm(
                    alarmType: typeDefault,
                    alarmLevel: level,
                    sourceType: SourceType.USER,
                    sourceCode: e.idCardNo,
                    alarmTime: current,
                    updateTime: current,
                    note: note
            ).save(flush: true)

            new WorkOrder(
                    sn: this.getSn(),
                    alarmType: typeDefault,
                    alarmLevel: level,
                    companyCode: e.companyCode,
                    userID: e.idCardNo,
                    checkTime: current,
                    rectificationTime: current + 5,
                    note: note
            ).save(flush: true)
        }
    }

    /**
     * 违法案件检查
     */
    private illegalInspection() {
        String sqlStr = """
            WITH cases AS (
                SELECT id_card_no,company_code
                FROM case_register_registerreport reg 
                LEFT JOIN case_finshcase_finishreport fin ON reg.case_register_no = fin.case_register_no
                WHERE fin.case_register_no IS NULL
                GROUP BY id_card_no,company_code)
            SELECT cases.id_card_no idCardNo,cases.company_code companyCode
            FROM cases
            INNER JOIN people_basicinfo_public pub ON cases.id_card_no=pub.id_card_no"""

        def resultList = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(sqlStr)
        }?.collect({ obj ->
            [
                    idCardNo: obj.idCardNo,
                    companyCode: obj.companyCode
            ]
        })

        AlarmType type = AlarmType.findByCodeNum("211")
        AlarmLevel level = AlarmLevel.SERIOUS
        def note = "有未结案信息，锁定营运资格"
        def current = new Date()

        resultList.each({ e ->
            new Alarm(
                    alarmType: type,
                    alarmLevel: level,
                    sourceType: SourceType.USER,
                    sourceCode: e.idCardNo,
                    alarmTime: current,
                    updateTime: current,
                    note: note
            ).save(flush: true)

            new WorkOrder(
                    sn: this.getSn(),
                    alarmType: type,
                    alarmLevel: level,
                    companyCode: e.companyCode,
                    userID: e.idCardNo,
                    checkTime: current,
                    rectificationTime: current + 5,
                    note: note
            ).save(flush: true)
        })
    }

    /**
     * 生成工单号
     */
    private static getSn() {
        def sn = System.currentTimeMillis() + "" + System.nanoTime().toString().toString()[-6..-1] + new Random().nextInt(100000).toString().padLeft(5, '0')
        return sn
    }

    /**
     * 根据表名获取sql,用于资格到期时间查询
     * @param tablename
     * @return
     */
    private static getSql(String tablename) {
        String sql = """SELECT id_card_no idCardNo,company_code companyCode,end_time endTime
                FROM ${tablename} 
                WHERE end_time < cast(:range as timestamp)"""
        return sql
    }
}
