import com.hfyz.securityExtension.CustomSpringSecurityService
import com.hfyz.securityExtension.CustomUserDetailsService
import com.hfyz.securityExtension.TokenAuthenticationProvider
import com.hfyz.securityExtension.TokenProcessingFilter
import grails.util.Environment
import org.springframework.boot.context.embedded.FilterRegistrationBean
import com.commons.io.FileManagerLocalImpl
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

    switch(Environment.current) {
        case Environment.PRODUCTION:
            fileManager(FileManagerLocalImpl){
                grailsApplication = application
            }
            break
        case Environment.DEVELOPMENT:
            fileManager(FileManagerLocalImpl){
                grailsApplication = application
            }
            break
        default:
            fileManager(FileManagerLocalImpl){
                grailsApplication = application
            }
            break
    }
}
