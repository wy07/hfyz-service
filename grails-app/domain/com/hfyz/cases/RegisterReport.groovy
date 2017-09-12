package com.hfyz.cases

/**
 * 案件 - 立案报告
 */
class RegisterReport {
    String caseRegisterNo //案件登记号
    String caseSource //案件来源
    String party //当事人
    String gender //性别
    Integer age //年龄
    String idCardNo //身份证号
    String workLicenseType //从业资格类别
    String workLicenseNo //从业资格证号
    String address //地址
    String phone //电话
    String ownerName //业户（单位）名称
    String ownerAddress //业户（单位）地址
    String businessLicenseCharacter //经营许可证字
    String businessLicenseNo //经营许可证号
    String legalRepresentative //法人代表
    String carLicenseNo //车辆（挂车）号牌
    String carPlateColor //车辆颜色
    String transportLicenseCharacter // 道路运输证字
    String transportLicenseNo //道路运输证号
    String carType //车辆类型
    String carBusinessOrg //车籍地运管机构
    Date illegalTime //违法时间
    String illegalAddress //违法地点
    String legalName //法律法规名称
    String item //条、款、项
    String illegalContent //违法内容
    String caseReason //案由
    String underTaker //承办人（执法人员）
    Date registerTime //立案日期
    String remark //备注
    String companyCode //业户编码

    static constraints = {
        caseRegisterNo maxSize: 17, nullable: false, blank: false, unique: true
        caseSource maxSize: 8, nullable: true, blank: false
        party maxSize: 30, nullable: true, blank: false
        gender maxSize: 12, nullable: true, blank: false
        age nullable: true
        idCardNo maxSize: 18, nullable: false, blank: false
        workLicenseType maxSize: 30, nullable: true, blank: false
        workLicenseNo maxSize: 12, nullable: true, blank: false
        address maxSize: 50, nullable: true, blank: false
        phone maxSize: 30, nullable: true, blank: false
        ownerName maxSize: 50, nullable: true, blank: false
        ownerAddress maxSize: 100, nullable: true, blank: false
        businessLicenseCharacter maxSize: 2, nullable: true, blank: false
        businessLicenseNo maxSize: 12, nullable: true, blank: false
        legalRepresentative maxSize: 30, nullable: true, blank: false
        carLicenseNo maxSize: 12, nullable: true, blank: false
        carPlateColor maxSize: 4, nullable: true, blank: false
        transportLicenseCharacter maxSize: 2, nullable: true, blank: false
        transportLicenseNo maxSize: 12, nullable: true, blank: false
        carType maxSize: 14, nullable: true, blank: false
        carBusinessOrg maxSize: 50, nullable: true, blank: false
        illegalTime nullable: true
        illegalAddress maxSize: 50, nullable: true, blank: false
        legalName maxSize: 40, nullable: true, blank: false
        item maxSize: 30, nullable: true, blank: false
        illegalContent maxSize: 300, nullable: true, blank: false
        caseReason maxSize: 300, nullable: true, blank: false
        underTaker maxSize: 30, nullable: true, blank: false
        registerTime nullable: true
        remark maxSize: 500, nullable: true, blank: false
        companyCode maxSize: 30, nullable: false, blank: false
    }

    static mapping = {
        table "CASE_REGISTER_REGISTERREPORT"
        version false
    }
}
