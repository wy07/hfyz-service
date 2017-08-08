grails {
    plugin {
        springsecurity {
            userLookup {
                userDomainClassName = 'com.hfyz.security.User'
                authorityJoinClassName = 'com.hfyz.security.UserRole'
                passwordPropertyName = 'passwordHash'
            }

            authority.className = 'com.hfyz.security.Role'
            requestMap.className = 'com.hfyz.security.PermissionGroup'
            securityConfigType = 'Requestmap'       //将权限字符串保存在数据库中，修改时需要显示的刷新。
            //配置使用hash编码的方式保存密码
            password {
                algorithm = 'SHA-256'
                hash.iterations = 1
            }
            dao.reflectionSaltSourceProperty = 'salt'


            filterChain.filterNames = [
                    'securityContextPersistenceFilter', 'logoutFilter',
                    'authenticationProcessingFilter',
                    'tokenProcessingFilter',
                    'rememberMeAuthenticationFilter', 'anonymousAuthenticationFilter',
                    'exceptionTranslationFilter', 'filterInvocationInterceptor'
            ]


            providerNames = [
                    'daoAuthenticationProvider'
                    , 'tokenAuthenticationProvider'
                    , 'anonymousAuthenticationProvider'
                    , 'rememberMeAuthenticationProvider']

            controllerAnnotations.staticRules = [
                    [pattern: '/', access: ['permitAll']],
                    [pattern: '/error', access: ['permitAll']],
                    [pattern: '/index', access: ['permitAll']],
                    [pattern: '/index.gsp', access: ['permitAll']],
                    [pattern: '/shutdown', access: ['permitAll']],
                    [pattern: '/assets/**', access: ['permitAll']],
                    [pattern: '/**/js/**', access: ['permitAll']],
                    [pattern: '/**/css/**', access: ['permitAll']],
                    [pattern: '/**/images/**', access: ['permitAll']],
                    [pattern: '/**/favicon.ico', access: ['permitAll']],
                    [pattern: '/login', access: ['permitAll']],
                    [pattern: '/login/**', access: ['permitAll']],
                    [pattern: '/**/**', access: ['permitAll']]
//                    [pattern: '/**/**', access: 'ROLE_ADMIN']
            ]

            filterChain.chainMap = [
                    [pattern: '/assets/**', filters: 'none'],
                    [pattern: '/**/js/**', filters: 'none'],
                    [pattern: '/**/css/**', filters: 'none'],
                    [pattern: '/**/images/**', filters: 'none'],
                    [pattern: '/favicon.ico', filters: 'none'],
                    [pattern: '/**/favicon.ico', filters: 'none'],
                    [pattern: '/login', filters: 'JOINED_FILTERS,-securityContextPersistenceFilter,-logoutFilter,-authenticationProcessingFilter,-tokenProcessingFilter,-rememberMeAuthenticationFilter,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-filterInvocationInterceptor'],
                    [pattern: '/login/**', filters: 'JOINED_FILTERS,-securityContextPersistenceFilter,-logoutFilter,-authenticationProcessingFilter,-tokenProcessingFilter,-rememberMeAuthenticationFilter,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-filterInvocationInterceptor'],
                    [pattern: '/auth/**', filters: 'JOINED_FILTERS,-securityContextPersistenceFilter,-logoutFilter,-authenticationProcessingFilter,-tokenProcessingFilter,-rememberMeAuthenticationFilter,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-filterInvocationInterceptor'],
                    [pattern: '/system-codes/getmenu', filters: 'none'],
                    [pattern: '/systemCode/getmenu', filters: 'none'],
                    [pattern: '/**', filters: 'none']
//                    [pattern: '/**', filters: 'JOINED_FILTERS'],
            ]
        }
    }
}











