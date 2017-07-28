package hfyz

import com.commons.utils.ConfigUtil

class BootStrap {
    def initService

    def init = { servletContext ->
        initService.initData()
        ConfigUtil.reLoadConfig()
    }
    def destroy = {
    }
}



