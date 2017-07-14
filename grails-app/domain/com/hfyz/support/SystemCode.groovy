package com.hfyz.support

class SystemCode {

    String name
    String codeNum


    static constraints = {
        name nullable: false, blank: false, maxSize: 30
        codeNum(nullable:false, maxSize:20, blank:false,validator: { val, obj , errors ->
            def a = obj.class.findByCodeNum(val)
            if(a && obj.id != a.id ){
                errors.rejectValue('codeNum', 'systemCode.codeNum.error.label','在当前分类中该编码的数据已存在，不能重复添加。')
            }
        })

    }

    static mapping = {
        discriminator column: 'type', value: "SYSTEM_CODE"
    }

    String toString() {
        name
    }
}
