package com.hfyz.owner

import com.hfyz.support.SystemType

/**
 * 企业内部管理制度
 */

class CompanyRegulation {
    String companyCode   //企业编码
    String ownerName //业户名称
    String regulationName //制度名称
    String fileName //文件名称
    String fileType //文件类型
    Double fileSize //单位kb
    SystemType systemType
    Date dateCreated
    String fileRealPath //上传文件真实路径
    static constraints = {
        companyCode blank: false, maxSize: 10
        regulationName unique: 'companyCode', nullable: false, blank: false
        fileName unique: 'companyCode', nullable: false, blank: false
    }
}
