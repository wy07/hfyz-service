import com.hfyz.securityExtension.CustomSpringSecurityService
import com.hfyz.securityExtension.CustomUserDetailsService
import com.hfyz.securityExtension.TokenAuthenticationProvider
import com.hfyz.securityExtension.TokenProcessingFilter
import org.springframework.boot.context.embedded.FilterRegistrationBean

// Place your Spring DSL code here
beans = {
    userDetailsService(CustomUserDetailsService)

    tokenProcessingFilter(TokenProcessingFilter) {
        authenticationManager = authenticationManager
    }

    tokenProcessingFilterDeregistrationBean(FilterRegistrationBean) {
        filter = ref('tokenProcessingFilter')
        enabled = false
    }

    tokenAuthenticationProvider(TokenAuthenticationProvider) {
        userDetailsService = ref('userDetailsService');
    }

    springSecurityService(CustomSpringSecurityService){
        authenticationTrustResolver = ref('authenticationTrustResolver')
        grailsApplication = application
        objectDefinitionSource = ref('objectDefinitionSource')
        passwordEncoder = ref('passwordEncoder')
    }
}
