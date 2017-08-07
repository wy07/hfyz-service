package com.hfyz.people

import com.commons.utils.SQLHelper
import com.hfyz.support.AlarmType
import com.hfyz.warning.Alarm
import com.hfyz.warning.AlarmLevel
import com.hfyz.warning.SourceType
import com.hfyz.workOrder.WorkOrder
import grails.async.Promise
import grails.transaction.Transactional

import static grails.async.Promises.task

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
    def getPeopleList(tableName, name, phoneNo, idCardNo, max, offset) {
        def params = [:]
        if (name) {
            params.name = name
        }
        if (phoneNo) {
            params.phoneNo = phoneNo
        }
        if (idCardNo) {
            params.idCardNo = idCardNo
        }
        Promise peopleCount = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.firstRow(getCountSql(tableName, name, idCardNo, phoneNo), params).count ?: 0
            }
        }

        Promise peopleList = task {
            SQLHelper.withDataSource(dataSource) { sql ->
                sql.rows(getListSql(tableName, name, idCardNo, phoneNo), params + [max: max, offset: offset])
            }?.collect({ obj ->
                [
                        name           : obj.peopleName,
                        gender         : obj.gender,
                        IDCardNo       : obj.idCardNo,
                        birthday       : obj.birthday?.format("yyyy-MM-dd"),
                        nation         : obj.nation,
                        nativePlace    : obj.nativePlace,
                        technologyTitle: obj.technologyTitle,
                        phoneNo        : obj.phoneNo,
                ]
            })
        }

        return [resultList: peopleList.get(), total: peopleCount.get()]
    }

    /**
     * 列表sql
     * @param tableName
     * @param name
     * @param idCardNo
     * @param phoneNo
     * @return
     */
    private static String getListSql(String tableName, String name, String idCardNo, String phoneNo) {
        String joinSql = tableName ? "LEFT JOIN  ${tableName} pep ON pub.id_card_no=pep.id_card_no" : ""
        String listSql = """
            SELECT 
            pub.name peopleName,
            pub.gender gender,
            pub.id_card_no idCardNo,
            pub.birthday birthday,
            pub.nation nation,
            pub.native_place nativePlace,
            pub.technology_title technologyTitle,
            pub.phone_no phoneNO  
            FROM people_basicinfo_public pub  ${joinSql} 
            WHERE 1=1 """
        if (tableName) {
            listSql += " AND pep.id_card_no IS NOT NULL "
        }
        if (name) {
            listSql += " AND pub.name=:name"
        }
        if (idCardNo) {
            listSql += " AND pub.id_card_no=:idCardNo"
        }
        if (phoneNo) {
            listSql += " AND pub.phone_no=:phoneNo"
        }
        listSql += " limit :max offset :offset"
        return listSql
    }
    /**
     * 统计总数sql
     * @param tableName
     * @param name
     * @param idCardNo
     * @param phoneNo
     * @return
     */
    private static String getCountSql(String tableName, String name, String idCardNo, String phoneNo) {
        String joinSql = tableName ? "LEFT JOIN  ${tableName} pep ON pub.id_card_no=pep.id_card_no" : ""
        String countSql = "SELECT COUNT(*) FROM people_basicinfo_public pub  ${joinSql} WHERE 1=1 "
        if (tableName) {
            countSql += " AND pep.id_card_no IS NOT NULL "
        }
        if (name) {
            countSql += " AND pub.name=:name"
        }
        if (idCardNo) {
            countSql += " AND pub.id_card_no=:idCardNo"
        }
        if (phoneNo) {
            countSql += " AND pub.phone_no=:phoneNo"
        }
        return countSql
    }
    /**
     * 查看详情
     * @param idCardNo
     */
    def getDetailInfo(idCardNo) {
        def result = [:]

        def checkMember = WorkerCheckMember.findByIdCardNo(idCardNo)
        def coach = WorkerCoach.findByIdCardNo(idCardNo)
        def driver = WorkerDriver.findByIdCardNo(idCardNo)
        def manager = WorkerManager.findByIdCardNo(idCardNo)
        def tech = WorkerTechnology.findByIdCardNo(idCardNo)
        def waiter = WorkerWaiter.findByIdCardNo(idCardNo)

        result["checkMember"] = checkMember.asType(Map)
        result["coach"] = coach.asType(Map)
        result["driver"] = driver.asType(Map)
        result["manager"] = manager.asType(Map)
        result["tech"] = tech.asType(Map)
        result["waiter"] = waiter.asType(Map)

        return result
    }

    /***********************************从业人员资格巡检**********************************/
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
                SELECT DISTINCT id_card_no,company_code
                FROM case_register_registerreport reg 
                LEFT JOIN case_finshcase_finishreport fin ON reg.case_register_no = fin.case_register_no
                WHERE fin.case_register_no IS NULL)
            SELECT cases.id_card_no idCardNo,cases.company_code companyCode
            FROM cases
            INNER JOIN people_basicinfo_public pub ON cases.id_card_no=pub.id_card_no """

        def resultList = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(sqlStr)
        }?.collect({ obj ->
            [
                    idCardNo   : obj.idCardNo,
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
