package com.hfyz.statistic

import com.commons.utils.ControllerHelper

class AlarmInfoStatisticController implements ControllerHelper {

    def list() {
        def result = []
        def random = new Random()
        12.times {
            def data = [
                    date: "2017-${it + 1}-1",
                    total: 1000 + random.nextInt(50),
                    passengerCharterCrossborder: random.nextInt(1000),
                    passengerTravelBusNormal: random.nextInt(20),
                    passengerTravelBusAbnormal: random.nextInt(100),
                    passengerTravelBusUndeclared: random.nextInt(300),
                    passengerTwoToFiveNotStop: random.nextInt(50),
                    passengerLocateFault: random.nextInt(100),
                    passengerOverSpeed: random.nextInt(50),
                    dancargoErouterNormal: random.nextInt(70),
                    dancargoErouterAbnormal: random.nextInt(200),
                    dancargoErouterUndeclared: random.nextInt(80),
                    dancargoTwoToFiveNotStop: random.nextInt(50),
                    dancargoLocateFault: random.nextInt(100),
                    dancargoOverSpeed: random.nextInt(30),
                    safetyAccidents: random.nextInt(20)
            ]
            result << data
        }
        renderSuccessesWithMap([resultList: result])
    }
}
