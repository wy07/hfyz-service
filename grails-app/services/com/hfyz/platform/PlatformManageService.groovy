package com.hfyz.platform

import com.commons.utils.SQLHelper
import grails.transaction.Transactional

@Transactional
class PlatformManageService {

    def dataSource
    def getPlatformList(){
        def result=[]
        def GET_PLATFORM_SQL = "select * from platform_manage"
        def platform=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_PLATFORM_SQL.toString())
        }
        def platformList
        platformList=PlatformManage.list(sort: "id")
        platformList.each{
            result << [id:it.id
                       ,ip:it.ip
                       ,port:it.port
                       ,name:it.name
                       ,code:it.code
                       ,contactName:it.contactName
                       ,contactPhone:it.contactPhone
                       ,draftPeople:it.draftPeople
                       ,contactPhone:it.contactPhone
                       ,status:it.status]
        }
        return result
    }

    def serviceMethod() {

    }
}
