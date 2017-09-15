package com.hfyz.support

import com.hfyz.security.User

class OwnerCheckRecord {

    boolean auto=false
    String companyCode
    Date dateCreated
    String question
    String answer
    boolean responsed=false
    User operator
    Date responseDate
    String responseContent
    Integer responseTime

    static constraints = {
        companyCode nullable: false, blank: false, maxSize: 20
        question nullable: false, blank: false, maxSize: 50
        answer nullable: false, blank: false, maxSize: 50
        responseDate nullable: true
        responseContent nullable: true, blank: false, maxSize: 50
        operator nullable: true, validator: {val, obj, errors ->
            if (!val && !obj.auto) {
                errors.rejectValue('operator', 'ownerCheckRecord.operator.invalid.validator.message','手动查岗必须有查岗人员！')
            }
        }
        responseTime nullable: true, max:310
    }

    static mapping = {
        comment '查岗信息表'
        id generator:'native', params:[sequence:'owner_check_record_seq'], defaultValue: "nextval('owner_check_record_seq')"
        auto comment: '查岗类型'
        companyCode comment: '业户编码'
        dateCreated comment: '查岗时间'
        question comment: '查岗内容'
        answer comment: '问题答案'
        responsed comment: '信息状态'
        operator comment: '查岗人'
        responseDate comment: '应答时间'
        responseContent comment: '应答内容'
        responseTime comment: '响应时间(秒)'
    }
}
