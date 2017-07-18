package com.hfyz.platform

import com.commons.utils.SQLHelper
import grails.transaction.Transactional

@Transactional
class PlatformManageService {

    def dataSource

    def getPlatformList() {
        def result = []
        def GET_PLATFORM_SQL = "select * from platform_manage"
        SQLHelper.withDataSource(dataSource) { sql -> sql.rows(GET_PLATFORM_SQL.toString()) }
        def platformList = PlatformManage.list(sort: "id")
        platformList.each {
            result << [id            : it.id
                       , ip          : it.ip
                       , port        : it.port
                       , name        : it.name
                       , code        : it.code
                       , contactName : it.contactName
                       , contactPhone: it.contactPhone
                       , draftPeople : it.draftPeople
                       , contactPhone: it.contactPhone
                       , status      : it.status
                       , carNum      : it.carNum
                       , onLineNum   : it.onLineNum
                       , allOnLineNum: it.allOnLineNum
                       , illegalNum  : it.illegalNum
                       , outLineNum  : it.outLineNum]
        }
        return result
    }

    def getPlatformByNameCode(String name, String code) {
        def paltformList = PlatformManage.createCriteria().list {
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
             , contactPhone: it.contactPhone
             , draftPeople : it.draftPeople
             , contactPhone: it.contactPhone
             , status      : it.status
             , carNum      : it.carNum
             , onLineNum   : it.onLineNum
             , allOnLineNum: it.allOnLineNum
             , illegalNum  : it.illegalNum]

        }
        return paltformList
    }

}
