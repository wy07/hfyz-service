package com.hfyz.security

import com.hfyz.support.Organization
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

//@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role{
	String authority
	String name
	Date dateCreated
    Date lastUpdated
	Organization org

	static constraints = {
		authority blank: false, unique: true,maxSize:30
		name blank: false, unique: true,maxSize:20
		org nullable: true
	}

	static mapping = {
		comment '角色信息表'
		cache true
		id generator:'native', params:[sequence:'role_seq'], defaultValue: "nextval('role_seq')"
		authority comment: '角色权限'
		name comment: '角色名称'
		dateCreated comment: '创建日期'
		lastUpdated comment: '最后更新时间'
		org comment: '所属组织机构'
	}

	String toString() {
		authority
	}
}
