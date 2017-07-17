package com.hfyz.securityExtension

import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenProcessingFilter extends GenericFilterBean implements Filter {
    def authenticationManager

    void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req
        HttpServletResponse response = (HttpServletResponse) res

        def sub = request.JSON?.token?.sub
        if (sub) {
            try {
                AuthenticationToken authRequest = new AuthenticationToken(sub)
                authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(request))
                Authentication authResult = authenticationManager.authenticate(authRequest)
                SecurityContextHolder.getContext().setAuthentication(authResult)
                chain.doFilter(request, response)
            } catch (AuthenticationException e) {
                SecurityContextHolder.clearContext()
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response)
        }
    }
}
