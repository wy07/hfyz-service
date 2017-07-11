package com.securityExtension

import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails

public class TokenAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (AuthenticationToken.class.isAssignableFrom(authentication))
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        AuthenticationToken signedToken = (AuthenticationToken) authentication
        if (!signedToken.getPrincipal()) {
            throw new BadCredentialsException(messages.getMessage("SubAuthenticationProvider.missingPrincipal", "Missing request sub"), userDetails() ? userDetails : null);
        }
    }

}
