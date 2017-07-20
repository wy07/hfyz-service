package com.hfyz

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils

class CarController implements ControllerHelper {
    def carService

    def search() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        def result = carService.search(request.JSON.businessType, request.JSON.licenseNo, max, offset)
        renderSuccessesWithMap([carList: result.carList, carCount: result.carCount])
    }
}
