package com.hfyz.owner
/**
 * 企业内部管理制度
 */

class CompanyRegulation {
    String companyCode   //企业编码
    String ownerName //业户名称
    String regulationName //制度名称
    String fileName //文件名称
    String fileType //文件名称
    Double fileSize //单位kb
    Date dateCreated

    static constraints = {
        companyCode unique: true, blank: false, maxSize: 10
    }
}
