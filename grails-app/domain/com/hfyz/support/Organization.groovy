package com.hfyz.support

class Organization {
    String name
    String code
    Organization parent

    static constraints = {
        name nullable: false, blank: false, maxSize: 30,unique: 'parent'
        code nullable: false, blank: false, maxSize: 20,unique: true
        parent nullable: true
    }

    static mapping = {
        comment '组织机构表'
        id generator:'native', params:[sequence:'organization_seq'], defaultValue: "nextval('organization_seq')"
        name comment: '组织机构名称'
        code comment: '组织机构代码'
        parent comment: '父级组织机构'
    }

}
