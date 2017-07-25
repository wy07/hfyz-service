package com.hfyz.people
/**
 * 从业人员-基本信息-公用信息
 */
class PeopleBasicInfo {
    String name //姓名
    String gender //性别
    Date birthday //出生日期
    String idCardNo //身份证号码
    String picture //照片
    String nation //民族
    String nativePlace //籍贯
    String phoneNo //联系电话
    String address //联系地址
    String email //电子邮件
    String postCode //邮政编码
    String educationLevel //文化程度
    String technologyTitle //技术职称
    String healthState //健康状况
    static constraints = {
        name maxSize: 30, nullable: true, blank: false
        gender maxSize: 12, nullable: true, blank: false
        birthday nullable: true
        idCardNo maxSize: 18, unique: true
        picture nullable: true
        nation maxSize: 10, nullable: true, blank: false
        nativePlace maxSize: 50, nullable: true, blank: false
        phoneNo maxSize: 30, nullable: true, blank: false
        address maxSize: 100, nullable: true, blank: false
        email maxSize: 50, nullable: true, blank: false
        postCode maxSize: 6, nullable: true, blank: false
        educationLevel maxSize: 20, nullable: true, blank: false
        technologyTitle maxSize: 20, nullable: true, blank: false
        healthState maxSize: 10, nullable: true, blank: false
    }
    static mapping = {
        table "PEOPLE_BASICINFO_PUBLIC"
        version false
    }
}
