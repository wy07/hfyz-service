package com.hfyz.owner

import java.sql.Blob

/**
 * 经营业户_基本信息_业户标识
 */

class OwnerIdentity {
    String ownerName                  //业户名称*
    String companyCode                //业户编码(组织机构代码）*
    String parentCompanyName          //上级企业名称
    String ownerAddress               //业户地址
    Number postCode                   //邮政编码
    String administrativeDivisionName //行政区划名称
    Number administrativeDivisionCode //行政区划代码
    String economicType               //经济类型
    String legalRepresentative        //法人代表
    String idCardType                 //法人代表身份证类型
    String idCardNo                   //法人代表身份证号
    Blob picture                      //法人代表照片
    String operateManager             //经营负责人 *
    String phone                      //电话  *
    String fax                        //传真号码
    String telephone                  //手机号码
    String email                      //电子邮箱
    String website                    //网址
    String parentOwner                //母公司
    String shortName                  //业户简称
    String ownerCode                  //企业单位代码

    static constraints = {
        ownerName unique: true, blank: false, maxSize: 100                   //业户名称
        companyCode unique: true, blank: false, maxSize: 10                    //业户编码(组织机构代码）
        parentCompanyName nullable: true, blank: false, maxSize: 100         //上级企业名称
        ownerAddress nullable: true, blank: false, maxSize: 100              //业户地址
        postCode nullable: true, maxSize: 6                                     //邮政编码
        administrativeDivisionName nullable: true, blank: false, maxSize: 50 //行政区划名称
        administrativeDivisionCode nullable: true, maxSize: 6                   //行政区划代码
        economicType nullable: true, blank: false, maxSize: 30               //经济类型
        legalRepresentative nullable: true, blank: false, maxSize: 30        //法人代表
        idCardType nullable: true, blank: false, maxSize: 30                 //法人代表身份证类型
        idCardNo nullable: true, blank: false, maxSize: 30                   //法人代表身份证号
        picture nullable: true                                               //法人代表照片
        operateManager nullable: true, blank: false, maxSize: 30             //经营负责人
        phone nullable: true, blank: false, maxSize: 30                      //电话
        fax nullable: true, blank: false, maxSize: 30                        //传真号码
        telephone nullable: true, blank: false, maxSize: 11                  //手机号码
        email nullable: true, blank: false, maxSize: 50                      //电子邮箱
        website nullable: true, blank: false, maxSize: 50                    //网址
        parentOwner nullable: true, blank :false                             //母公司
        shortName nullable: true, blank :false                             //业户简称
        ownerCode nullable: true, blank :false                             //企业单位代码

    }
}
