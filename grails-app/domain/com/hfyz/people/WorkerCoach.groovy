package com.hfyz.people
/**
 * 从业人员-教练员
 */
class WorkerCoach {
    String idCardNo //身份证号
    String companyCode //业户编码
    String workLicenseType //从业资格类别
    String workLicenseNo //从业资格证号
    Date workLicenseGetTime //从业资格初领时间
    Date workLicenseGrantTime //从业资格发放时间
    Date endTime //证件有效期至
    String driveLicenseNo //驾驶证号
    String driveCarType //准驾类型
    Date driveLicenseGetTime //驾驶证初领时间
    String licenseGrantOrga //发证机关
    String licenseSituation //证照状态
    String workSituation //从业状态
    String ownerName //业户名称
    String businessPermitCharacter //经营许可证字
    String businessPermitNo //经营许可证号
    Integer changeTimes //补换证次数
    Integer trainTimes //培训次数
    String inspectDealSituation //稽查处理状态
    String teachType //准教类型
    String teachCarType //准教车型
    String driveLicenseSituation //证照状态

    static constraints = {
        idCardNo maxSize: 18, unique: true, blank: false
        companyCode nullable: false, blank: false
        workLicenseType maxSize: 30, nullable: true, blank: false
        workLicenseNo maxSize: 12, nullable: true, blank: false
        workLicenseGetTime nullable: true
        workLicenseGrantTime nullable: true
        endTime nullable: true
        driveLicenseNo maxSize: 18, nullable: true, blank: false
        driveCarType maxSize: 14, nullable: true, blank: false
        driveLicenseGetTime nullable: true
        licenseGrantOrga nullable: true, maxSize: 50, blank: false
        licenseSituation maxSize: 4, nullable: true, blank: false
        workSituation maxSize: 8, nullable: true, blank: false
        ownerName maxSize: 50, nullable: true, blank: false
        businessPermitCharacter maxSize: 2, nullable: true, blank: false
        businessPermitNo maxSize: 12, nullable: true, blank: false
        changeTimes nullable: true
        trainTimes nullable: true
        inspectDealSituation nullable: true
        teachType maxSize: 8, nullable: true, blank: false
        teachCarType maxSize: 14, nullable: true, blank: false
        driveLicenseSituation maxSize: 4, nullable: true, blank: false
    }

    static mapping = {
        table "PEOPLE_WORKER_COACH"
        version false
        inspectDealSituation sqlType: char, length: 1
    }
}
