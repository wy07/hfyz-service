package com.hfyz.owner
/**
 * 企业内部管理制度
 */

class CompanyRegulation {
    String companyCode   //企业编码

    static constraints = {
        companyCode unique: true, blank: false, maxSize: 10
    }
}
