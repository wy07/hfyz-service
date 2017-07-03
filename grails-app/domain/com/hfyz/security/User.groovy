package com.hfyz.security

import com.commons.hibernate.JsonbListType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.hfyz.support.Organization

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService

	String username
	String name
	String tel
	String email
	String password
	List    rights     //权限
	Date dateCreated
    Date lastUpdated 
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	Organization org
	int  operator  //操作员id

	User(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}

	/*不起作用
	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}*/

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true,maxSize:20
		tel nullable:true,blank: true, unique: true,maxSize:11
		email nullable:true,blank: true, unique: true,email:true,maxSize:50
		name nullable:false,blank: false,maxsize:20
		password blank: false
		rights nullable:true
		org nullable:true
		operator nullable:true
	}

	static mapping = {
		table 'sys_user'
		id generator:'native', params:[sequence:'user_seq'], defaultValue: "nextval('user_seq')"
		password column: '`password`'
		rights   type: JsonbListType,sqlType: 'jsonb'
	}
}
