package com.hfyz.support

class Menu {
    String name
    String code
    String style
    String icon
    Menu parent
    String position
    Boolean display=true

    static constraints = {
        name nullable: false, blank: false, maxSize: 30
        code nullable: false, unique:true,blank: false, maxSize: 20
        style nullable: true, blank: false, maxSize: 60
        icon nullable: true, blank: false, maxSize: 15
        parent nullable: true,validator: { val, obj, errors ->
            if (val && obj.position !=val.position) {
                errors.rejectValue('parent', 'systemCode.parent.invalid.validator.message', '当前位置应与上层菜单一致')
            }
        }

        position nullable: false, blank: false, inList: ['TOP_BAR', 'SIDE_BAR']
        display nullable: false
    }
}
