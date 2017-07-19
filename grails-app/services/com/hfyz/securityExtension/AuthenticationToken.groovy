package com.hfyz.securityExtension

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class AuthenticationToken extends UsernamePasswordAuthenticationToken {

    public AuthenticationToken(String principal) {
        super(principal, null)
    }

}
