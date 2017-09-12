package com.hfyz.support

import grails.transaction.Transactional

@Transactional
class ElectricFenceService {

    def search(Map inputParams, Integer max, Integer offset) {


        def resultList = ElectricFence.createCriteria().list([max: max, offset: offset]) {

            if (inputParams.name) {
                like("name", "${inputParams.name}%")
            }

        }?.collect({ ElectricFence electricFence ->
            [
                    id         : electricFence.id,
                    name       : electricFence.name,
                    coordinates: electricFence.coordinates
            ]

        })

        def total = ElectricFence.createCriteria().get {
            projections {
                count()
            }

            if (inputParams.name) {
                like("name", "${inputParams.name}%")
            }
        }

        return [resultList: resultList, total: total]
    }
}
