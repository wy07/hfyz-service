package com.hfyz.owner
/**
 * 经营业户-基本信息-经营信息
 */
class OwnerManageInfo {
    String companyCode // 业户标识
    String licenseCharacter // 经营许可证字
    String licenseNo // 经营许可证号
    Date beginTime //有效期起
    Date endTime // 有效期止
    String grantOrganization // 发证机关
    Date checkTime //核发日期
    String domicileOperateName //户籍地运管机构名称
    String domicileOperateNo //户籍地运管机构代码
    String paymentSituation // 规费缴纳状态
    String licenseChangeTimes //征兆补换次数
    String inspectTreatmentSituation // 稽查处理状态

    static constraints = {
    }

    static mapping = {
        table "OWNER_BASICINFO_MANAGEINFO"
        version false
        paymentSituation sqlType: 'char'
        inspectTreatmentSituation sqlType: 'char'
    }
}
