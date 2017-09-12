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
        comment '数据字典表'
        id generator:'native', params:[sequence:'system_code_seq'], defaultValue: "nextval('system_code_seq')"
        discriminator column: 'type', value: "SYSTEM_CODE", comment: '数据字典类型'
        name comment: '数据字典名称'
        codeNum comment: '数据字典编码'
        parent comment: '父级数据字典'

    }

    String toString() {
        name
    }
}
