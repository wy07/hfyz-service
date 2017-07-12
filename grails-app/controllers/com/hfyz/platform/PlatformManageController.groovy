package com.hfyz.platform

import com.commons.utils.ControllerHelper

class PlatformManageController implements ControllerHelper{
    def platformManageService
    def list() {
        renderSuccessesWithMap([platformList: platformManageService.getPlatformList()])
    }

    def index() { }
}
