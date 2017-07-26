package hfyz

class BootStrap {
    def initService

    def init = { servletContext ->
        initService.initData()
    }
    def destroy = {
    }
}



