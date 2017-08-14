package com.hfyz.statistic

import com.commons.utils.ControllerHelper

class PlatformStatisticController implements ControllerHelper{

    def list() {
        renderSuccessesWithMap(platformList:[offline:66, failure:42, response:43])
    }
}
