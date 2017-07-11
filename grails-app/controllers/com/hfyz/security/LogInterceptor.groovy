package com.hfyz.security


class LogInterceptor {

    def  userAgentIdentService

    LogInterceptor() {
        matchAll()
    }

    boolean before() {
        def agent = ''
        if (userAgentIdentService.isMobile()) {
            agent = "Mobile Broswer"
        } else if (userAgentIdentService.isRobot()) {
            agent = "Robot"
        } else {
            agent = "Pc Browser"
        }
        session.setAttribute('agent', agent)
        params.operatorId = request.JSON?.token?.id
        true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
