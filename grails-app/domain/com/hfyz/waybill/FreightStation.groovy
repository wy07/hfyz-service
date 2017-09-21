package com.hfyz.waybill

import com.hfyz.support.DangerousType
import com.hfyz.support.FreightStationLevel
import com.hfyz.support.ManageStatus

/**
 *货运站--基本信息
 */
class FreightStation {
    String orgCode              //组织机构代码
    String name                 //货运站名称
    String cn                   //货运站编号
    ManageStatus manageStatus   //经营状态
    Date buildDate              //建站日期
    Date completedDate          //竣工日期
    Date checkDate              //验收日期
    Date operateDate            //投入营运日期
    BigDecimal scale            //投资规模 (万元)
    String approvalNumber       //批准文号
    String districtName         //行政区划名称
    String districtCode         //行政区划代码
    FreightStationLevel level   //货运站级别
    String address              //货运站地址
    String frontPhoto           //货运站正面照片
    String sidePhoto            //货运站侧面照片
    BigDecimal coverArea        //占地面积 m2
    BigDecimal buildArea       //建筑面积 m2
    BigDecimal height          //货运站高度 m

    static hasMany = [manageRange: DangerousType]  //经营范围

    static constraints = {
        cn unique: 'orgCode', nullable: false, blank: false
    }
}
