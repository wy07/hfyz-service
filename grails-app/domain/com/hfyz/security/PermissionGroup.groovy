package com.hfyz.security

import org.springframework.http.HttpMethod

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

//@GrailsCompileStatic
@EqualsAndHashCode(includes = ['configAttribute', 'httpMethod', 'url'])
@ToString(includes = ['configAttribute', 'httpMethod', 'url'], cache = true, includeNames = true, includePackage = false)
class PermissionGroup implements Serializable {

    private static final long serialVersionUID = 1


    String category
    String name
    String code
    String configAttribute
    HttpMethod httpMethod
    String url

    static constraints = {
        category nullable: false, blank: false, maxSize: 50
        name nullable: false, blank: false, maxSize: 20,unique: 'category'
        code nullable: false, blank: false, maxSize: 100, unique: true
        configAttribute blank: true, nullable: false, maxSize: 200
        httpMethod nullable: true, maxSize: 50
        url blank: false, unique: 'httpMethod', maxSize: 100
    }

    static mapping = {
        comment '权限信息表'
        id generator:'native', params:[sequence:'permission_group_seq'], defaultValue: "nextval('permission_group_seq')"
        category comment: '权限分类'
        name comment: '权限名称'
        code comment: '权限编码'
        configAttribute comment: '权限配置'
        httpMethod comment: '请求方法'
        url comment: '请求路径'
    }

    Object asType(Class clazz) {

        if (clazz == Map.class) {
            Map map = [
                    id               : this.id
                    , category       : this.category
                    , name           : this.name
                    , code           : this.code
                    , configAttribute: this.configAttribute
                    , url            : this.url
                    , httpMethod     : this.httpMethod?.toString()
            ]
            return map
        }

        return null
    }
}
