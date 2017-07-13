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
    Date responseTime

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
        responseTime nullable: true
    }
}
