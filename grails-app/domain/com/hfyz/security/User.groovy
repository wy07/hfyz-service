package com.hfyz.security

import com.commons.hibernate.JsonbListType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import com.hfyz.support.Organization
import sun.misc.BASE64Encoder
import java.security.SecureRandom

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService

	String username
	String name
	String tel
	String email
	String passwordHash                //加密后的密码
	String salt                        //随机盐
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

	def beforeUpdate() {
		if (isDirty('passwordHash')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		passwordHash = springSecurityService.encodePassword(passwordHash, salt)
	}

	static transients = ['springSecurityService']

	static constraints = {
		username blank: false, unique: true,maxSize:20
		tel nullable:true,blank: true, unique: true,maxSize:11
		email nullable:true,blank: true, unique: true,email:true,maxSize:50
		name nullable:false,blank: false,maxsize:20
		passwordHash blank: false
        salt blank: false
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

	static String getSecureRandomSalt() {
		byte[] bytes = new byte[24]
		new SecureRandom().nextBytes(bytes)
		return new BASE64Encoder().encode(bytes)
	}
}
