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
		comment '用户信息表'
		table 'sys_user'
		id generator:'native', params:[sequence:'user_seq'], defaultValue: "nextval('user_seq')"
		username comment: '用户名'
		name comment: '真实姓名'
		tel comment: '电话'
		email comment: '电子邮箱'
		passwordHash comment: '加密后的密码'
		salt comment: '随机盐'
		dateCreated comment: '创建时间'
		lastUpdated comment: '最后更新时间'
		org comment: '所属组织结构'
		companyCode comment: '所属业户编码'
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

	boolean isCompanyUser(){
		return this.org?.code=='24'
	}

	boolean isAdmin(){
		return this.org?.code== null
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
