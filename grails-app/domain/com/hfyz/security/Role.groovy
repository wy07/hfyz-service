package com.hfyz.security

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.hfyz.support.Organization
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

	private static final long serialVersionUID = 1

	String authority
	String name
	int  operator  //操作员id
	Date dateCreated
    Date lastUpdated 
	static hasMany = [orgs:Organization,permissionGroups: PermissionGroup]
	Role(String authority) {
		this()
		this.authority = authority
	}

	static constraints = {
		authority blank: false, unique: true,maxSize:20
		name blank: false, unique: true,maxSize:20
		operator nullable:true
	}

	static mapping = {
		cache true
		id generator:'native', params:[sequence:'role_seq'], defaultValue: "nextval('role_seq')"
		permissionGroups joinTable: [name: 'role_permission',
                                     key: 'role_id',
                                     column: 'permission_id',
                                     type: "bigint"]
	}
}
