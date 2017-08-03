package com.hfyz.people
/**
 * 从业人员--营运驾驶员
 */
class WorkerDriver {
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
        idCardNo maxSize: 18, unique: true
        companyCode nullable: false, blank: false
        workLicenseType maxSize: 30, nullable: true, blank: false
        workLicenseNo maxSize: 12, nullable: true, blank: false
        workLicenseGetTime nullable: true
        workLicenseGrantTime nullable: true
        endTime nullable: true
        driveLicenseNo maxSize: 18, nullable: true, blank: false
        driveCarType maxSize: 14, nullable: true, blank: false
        driveLicenseGetTime nullable: true
        licenseGrantOrg maxSize: 50, nullable: true, blank: false
        licenseSituation maxSize: 4, nullable: true, blank: false
        workSituation maxSize: 8, nullable: true, blank: false
        ownerName maxSize: 50, nullable: true, blank: false
        businessPermitCharacter maxSize: 2, nullable: true, blank: false
        businessPermitNo maxSize: 12, nullable: true, blank: false
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

    Object asType(Class c) {
        if (c == Map.class) {
            Map map = [
                    idCardNo               : this.idCardNo,
                    companyCode            : this.companyCode,
                    workLicenseType        : this.workLicenseType,
                    workLicenseNo          : this.workLicenseNo,
                    workLicenseGetTime     : this.workLicenseGetTime?.format('yyyy-MM-dd'),
                    workLicenseGrantTime   : this.workLicenseGrantTime?.format('yyy-MM-dd'),
                    endTime                : this.endTime?.format('yyyy-MM-dd'),
                    driveLicenseNo         : this.driveLicenseNo,
                    driveCarType           : this.driveCarType,
                    driveLicenseGetTime    : this.driveLicenseGetTime?.format('yyyy-MM-dd'),
                    licenseGrantOrg        : this.licenseGrantOrg,
                    licenseSituation       : this.licenseSituation,
                    workSituation          : this.workSituation,
                    ownerName              : this.ownerName,
                    businessPermitCharacter: this.businessPermitCharacter,
                    businessPermitNo       : this.businessPermitNo,
                    changeTimes            : this.changeTimes,
                    trainTimes             : this.trainTimes,
                    inspectDealSituation   : this.inspectDealSituation,
                    trafficAccidentRecordNo: this.trafficAccidentRecordNo
            ]
            return map;
        }
        return null;
    }
}
