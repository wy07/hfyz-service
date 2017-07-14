package com.hfyz.infoManage

import grails.transaction.Transactional

@Transactional
class InfoauditService {
    def getPublishList(operatorId) {
        def publishList=Infoaudit.createCriteria().list {
            publisher{
                eq('id',operatorId)
            }
        }?.collect{ Infoaudit infoaudit->
            [id:infoaudit.id
             ,type:infoaudit.type
             ,title:infoaudit.title
             ,dateCreated:infoaudit.dateCreated?.format('yyyy-MM-dd HH:mm:ss ')
             ,username:infoaudit.publisher.name
             ,status:infoaudit.status.type]
        }
        return publishList
    }

    def getSearchList(textTitle, dateBegin, dateEnd){
        println  textTitle
        println  dateBegin
        println  dateEnd
        def publishList = Infoaudit.createCriteria().list {
            if(textTitle){
                like("title", "${textTitle}%")
            }
            if(dateBegin){
                def myDateBegin = Date.parse("yyyy-MM-dd HH:mm", dateBegin)
                ge("dateCreated", myDateBegin)
            }
            if(dateEnd){
                def myDateEnd = Date.parse("yyyy-MM-dd HH:mm", dateEnd)
                le("dateCreated", myDateEnd)
            }
        }?.collect{ Infoaudit infoaudit->
            [id:infoaudit.id
             ,type:infoaudit.type
             ,title:infoaudit.title
             ,dateCreated:infoaudit.dateCreated?.format('yyyy-MM-dd HH:mm:ss ')
             ,username:infoaudit.publisher.name
             ,status:infoaudit.status.type]
        }
        return publishList
    }
}