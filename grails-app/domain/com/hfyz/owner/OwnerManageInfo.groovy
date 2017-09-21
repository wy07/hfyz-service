package com.hfyz.owner
/**
 * 经营业户-基本信息-经营信息
 */
class OwnerManageInfo {
    Date modifyTime                   //更新时间
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
    Date firstGrantTime //首次发证日期
    Date openTime //开业日期
    String manageScope //经营范围
    String ownerStatius//业户状态
    String examYear //审验状态
    Date examTime //年审时间
    String creditLevel //信誉等级
    String fileNumber //档案号
    String cityAbbreviation //地市简称
    String branchOrgAddress //分支机构及地址
    String remark //备注
    String orgCode // 业户标识

    static constraints = {
        remark nullable: true,blank: false
    }

    static mapping = {
        table "OWNER_BASICINFO_MANAGEINFO"
        version false
        paymentSituation sqlType: 'char'
        inspectTreatmentSituation sqlType: 'char'
    }
}
