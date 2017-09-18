package com.hfyz.platform

import com.commons.utils.SQLHelper
import grails.transaction.Transactional

@Transactional
class PlatformManageService {

    def getPlatformList(def max, def offset, String name, String code) {
        def platformList = PlatformManage.createCriteria().list([max: max, offset: offset, sort: 'code']) {
            if (name) {
                like("name", "${name}%")
            }
            if (code) {
                like("code", "${code}%")
            }

        }?.collect { PlatformManage it ->
            [id            : it.id
             , ip          : it.ip
             , port        : it.port
             , name        : it.name
             , code        : it.code
             , contactName : it.contactName
             , contactPhone: it.contactPhone]

        }

        def total = PlatformManage.createCriteria().get {
            projections {
                count()
            }
            if (name) {
                like("name", "${name}%")
            }
            if (code) {
                like("code", "${code}%")
            }
        }
        return [platformList: platformList, total: total]
    }


}
