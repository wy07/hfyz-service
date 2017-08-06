package com.hfyz.security

import com.hfyz.support.Organization
import sun.misc.BASE64Encoder
import java.security.SecureRandom

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

//@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

	transient springSecurityService

	String username
	String name
	String tel
	String email
	String passwordHash                //加密后的密码
	String salt                        //随机盐
	Date dateCreated
    Date lastUpdated
	Organization org
	String companyCode


	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	static mapping = {
		table 'sys_user'
		id generator:'native', params:[sequence:'user_seq'], defaultValue: "nextval('user_seq')"
	}

	static constraints = {
		username blank: false, unique: true,maxSize:20
		tel nullable:true,blank: true, unique: true,maxSize:11
		email nullable:true,blank: true, unique: true,email:true,maxSize:50
		name nullable:false,blank: false,maxsize:20
		passwordHash blank: false
		salt blank: false
		org nullable:true
		companyCode nullable: true, blank: false, maxSize: 10
	}

	static transients = ['springSecurityService']

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}
//
//	def beforeUpdate() {
//		if (isDirty('passwordHash')) {
//			encodePassword()
//		}
//	}

	protected void encodePassword() {
		passwordHash = springSecurityService.encodePassword(passwordHash, salt)
	}

	static String getSecureRandomSalt() {
		byte[] bytes = new byte[24]
		new SecureRandom().nextBytes(bytes)
		return new BASE64Encoder().encode(bytes)
	}
}
