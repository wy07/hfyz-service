package hfyz

import com.commons.utils.ConfigUtil
import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils

class BootStrap {
    def initService

    def init = { servletContext ->
        SpringSecurityUtils.clientRegisterFilter('tokenProcessingFilter', SecurityFilterPosition.OPENID_FILTER.order + 3)
        initService.initData()
        ConfigUtil.reLoadConfig()
    }
    def destroy = {
    }
}



