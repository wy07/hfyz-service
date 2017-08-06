package com.hfyz.security

import grails.transaction.Transactional
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

@Transactional
class LoginService {

    def authenticationManager

    def signIn(username, password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password)
        Authentication authentication = authenticationManager.authenticate(token)
        return authentication.getPrincipal()
    }

//    def TestUser addTestUser(String username, String... roles) {
//        def testUser = save(new TestUser(username: username, password: 'password'))
//        roles.each { save new TestUserTestRole(testUser: testUser, testRole: TestRole.findOrSaveByAuthority(it)) }
//        testUser
//    }


}