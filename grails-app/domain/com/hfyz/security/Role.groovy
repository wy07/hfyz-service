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
		cache true
		id generator:'native', params:[sequence:'role_seq'], defaultValue: "nextval('role_seq')"
	}

	String toString() {
		authority
	}
}
