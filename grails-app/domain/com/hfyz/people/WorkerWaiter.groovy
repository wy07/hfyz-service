package com.hfyz.people
/**
 * 从业人员--站场服务人员
 */
class WorkerWaiter {
    String IDCardNo //身份证号
    String jobName //岗位名称
    String jobLicenseNo //岗位证编号
    Date grantTime //发证日期
    Date beginWorkTime //上岗日期

    static constraints = {
        "IDCardNo" maxSize: 18, unique: true
        jobName maxSize: 50, nullable: true, blank: true
        jobLicenseNo maxSize: 50, nullable: true, blank: true
        grantTime nullable: true
        beginWorkTime nullable: true
    }

    static mapping = {
        table "PEOPLE_WORKER_WAITER"
        version false
    }
}