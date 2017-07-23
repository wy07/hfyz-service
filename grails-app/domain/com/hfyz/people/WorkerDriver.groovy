package com.hfyz.people
/**
 * 从业人员--营运驾驶员
 */
class WorkerDriver {
    String IDCardNo //身份证号
    String workLicenseType //从业资格类别
    String workLicenseNo //从业资格证号
    Date workLicenseGetTime //从业资格初领时间
    Date workLicenseGrantTime //从业资格发放时间
    Date endTime //证件有效期至
    String driveLicenseNo //驾驶证号
    String driveCarType //准驾类型
    Date driveLicenseGetTime //驾驶证初领时间
    String licenseGrantOrg //发证机关
    String licenseSituation //证照状态
    String workSituation //从业状态
    String ownerName //业户名称
    String businessPermitCharacter //经营许可证字
    String businessPermitNo //经营许可证号
    Integer changeTimes //补换证次数
    Integer trainTimes //培训次数
    String inspectDealSituation //稽查处理状态
    Integer trafficAccidentRecordNo //交通事故次数

    static constraints = {
        "IDCardNo" maxSize: 18, unique: true
        workLicenseType maxSize: 30, nullable: true, blank: true
        workLicenseNo maxSize: 12, nullable: true, blank: true
        workLicenseGetTime nullable: true
        workLicenseGrantTime nullable: true
        endTime nullable: true
        driveLicenseNo maxSize: 18, nullable: true, blank: true
        driveCarType maxSize: 14, nullable: true, blank: true
        driveLicenseGetTime nullable: true
        licenseGrantOrg maxSize: 50, nullable: true, blank: true
        licenseSituation maxSize: 4, nullable: true, blank: true
        workSituation maxSize: 8, nullable: true, blank: true
        ownerName maxSize: 50, nullable: true, blank: true
        businessPermitCharacter maxSize: 2, nullable: true, blank: true
        businessPermitNo maxSize: 12, nullable: true, blank: true
        changeTimes nullable: true
        trainTimes nullable: true
        inspectDealSituation nullable: true
        trafficAccidentRecordNo nullable: true
    }

    static mapping = {
        table "PEOPLE_WORKER_DRIVER"
        version false
        inspectDealSituation sqlType: "char", length: 1
    }
}
