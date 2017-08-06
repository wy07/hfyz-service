package com.securityExtension
import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class CustomUserDetails extends GrailsUser {

    final String salt
    final String companyCode


    CustomUserDetails(String username
                      , String password
                      , boolean enabled
                      , boolean accountNonExpired
                      , boolean credentialsNonExpired
                      , boolean accountNonLocked
                      , Collection<GrantedAuthority> authorities
                      , id
                      , String salt
                      , String companyCode) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities, id)
        this.salt = salt
        this.companyCode = companyCode
    }
}
