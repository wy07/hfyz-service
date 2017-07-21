package com.hfyz.people

class WorkerTechnology {
    String IDCardNo //身份证号
    String workLicenseType //从业资格类别
    String workLicenseNo //从业资格证号
    Date workLicenseGetTime //从业资格初领时间
    Date workLicenseGrantTime //从业资格发放时间
    Date endTime //证件有效期至
    String licenseGrantOrganization //发证机关
    String licenseSituation //证照机关
    String ownerName //业户名称
    String businessPermitCharacter //经营许可证字
    String businessPermitNo //经营许可证号
    Integer changeTimes //补换证次数
    Integer trainTimes //培训次数
    String inspectDealSituation //稽查处理状态
    Integer technologyDirector //是否技术负责人
    String technologyLevel //专业技术等级
    Date technologyLicenseGrantTime //专业技术证书发放时间

    static constraints = {
        "IDCardNo" maxSize: 18, unique: true
        workLicenseType maxSize: 30, nullable: true,blank:true
        workLicenseNo maxSize: 12, nullable: true,blank:true
        workLicenseGetTime nullable: true
        workLicenseGrantTime nullable: true,blank:true
        endTime nullable: true
        licenseGrantOrganization maxSize: 50, nullable: true,blank:true
        licenseSituation maxSize: 4, nullable: true,blank:true
        ownerName maxSize: 50, nullable: true,blank:true
        businessPermitCharacter maxSize: 2, nullable: true,blank:true
        businessPermitNo maxSize: 12, nullable: true,blank:true
        changeTimes nullable: true
        trainTimes nullable: true
        inspectDealSituation nullable: true
        technologyDirector nullable: true
        technologyLevel nullable: true,blank:true, maxSize: 4
        technologyLicenseGrantTime nullable: true
    }

    static mapping = {
        table "PEOPLE_WORKER_TECHNOLOGY"
        version false
        inspectDealSituation sqlType: char, length: 1
    }
}
