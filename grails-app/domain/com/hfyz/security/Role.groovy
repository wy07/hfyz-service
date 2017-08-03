package com.hfyz.security

import com.hfyz.support.Organization

class Role{

	String authority
	String name
	Date dateCreated
    Date lastUpdated
	Organization org

	static constraints = {
		authority blank: false, unique: true,maxSize:20
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
