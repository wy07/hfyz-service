package com.hfyz.people

import org.grails.validation.MaxSizeConstraint

/**
 * 从业人员-考核员
 */
class WorkerCheckMember {
    String idCardNo //身份证号
    String workLicenseType  //从业资格类别
    String workLicenseNo  //从业资格证号
    Date workLicenseGetTime  //从业资格证初领时间
    Date workLicenseGrantTime  //从业资格证发放时间
    Date endTime  //证件有效期至
    String licenseGrantOrg  //发证机关
    String licenseSituation  //证照状态
    Integer licenseChangeTimes  //补换证次数
    Integer trainTimes //培训次数
    String checkType //考核类别

    static constraints = {
        idCardNo maxSize: 18, unique: true
        workLicenseType maxSize: 20, nullable: true, blank: false
        workLicenseNo maxSize: 12, nullable: true, blank: false
        workLicenseGetTime nullable: true
        workLicenseGrantTime nullable: true
        endTime nullable: true
        licenseGrantOrg maxSize: 50, nullable: true, blank: false
        licenseSituation maxSize: 4, nullable: true, blank: false
        licenseChangeTimes nullable: true
        trainTimes nullable: true
        checkType maxSize: 4, nullable: true, blank: false
    }

    static mapping = {
        table "PEOPLE_WORKER_CHECKMEMBER"
        version false
    }
}
