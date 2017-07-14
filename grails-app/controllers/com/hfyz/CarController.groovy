package com.hfyz

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class CarController implements ControllerHelper {
    def carService

    def search() {
        int max = PageUtils.getMax(params.max, 10, 100)
        int offset = PageUtils.getOffset(params.offset)
        def result = carService.search(params.businessType, params.licenseNo, max, offset)
        renderSuccessesWithMap([carList: result.carList, carCount: result.carCount])
    }
}
