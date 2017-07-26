package com.hfyz.infoManage

import grails.transaction.Transactional

@Transactional
class InfoauditService {

    /*********** 信息发布、信息查询的信息列表初始化****************/
    def getPublishList( max, offset) {
        def publishList = Infoaudit.createCriteria().list([max: max, offset: offset]) {
        }?.collect { Infoaudit infoaudit->
            [id:infoaudit.id
             ,type:infoaudit.type
             ,title:infoaudit.title
             ,dateCreated:infoaudit.dateCreated?.format('yyyy-MM-dd HH:mm:ss ')
             ,username:infoaudit.publisher.name
             ,status:infoaudit.status.type]
        }

        def total = Infoaudit.createCriteria().get {
            projections {
                count()
            }
        }
        return [publishList: publishList, total: total]
    }


    /*********** 信息发布搜索功能****************/
    def getSearchList(textTitle, dateBegin, dateEnd, max, offset){
        println  textTitle
        println  dateBegin
        println  dateEnd
        def publishList = Infoaudit.createCriteria().list([max: max, offset: offset]) {
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

        def total = Infoaudit.createCriteria().get {
            projections {
                count()
            }
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
        }
        return [publishList: publishList, total: total]
    }


   /*********** 信息查询树形列表项搜索****************/
    def getSearchListN(type, max, offset){
        def publishList = Infoaudit.createCriteria().list([max: max, offset: offset]) {
            if(type){
                like("type", "${type}%")
            }
        }?.collect{ Infoaudit infoaudit->
            [id:infoaudit.id
             ,type:infoaudit.type
             ,title:infoaudit.title
             ,dateCreated:infoaudit.dateCreated?.format('yyyy-MM-dd HH:mm:ss ')
             ,username:infoaudit.publisher.name
             ,status:infoaudit.status.type]
        }

        def total = Infoaudit.createCriteria().get {
            projections {
                count()
            }
            if(type){
                like("type", "${type}%")
            }
        }
        return [publishList: publishList, total: total]
    }

}