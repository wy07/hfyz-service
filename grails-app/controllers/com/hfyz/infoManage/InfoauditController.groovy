package com.hfyz.infoManage

import com.commons.utils.ControllerHelper
import grails.transaction.Transactional

@Transactional(readOnly = true)
class InfoauditController implements ControllerHelper {

    def infoauditService
    def list() {
        renderSuccessesWithMap([publishList: infoauditService.getPublishList(request.JSON.max, request.JSON.offset)])
    }

    def save() {
        println request.JSON
        Infoaudit infoaudit = new Infoaudit(request.JSON)
        infoaudit.save(flush: true, failOnError: true)
        renderSuccess()
    }

    def edit() {
        println params
        withInfoaudit(params.long('id')) { infoaudit ->
            renderSuccessesWithMap([infoaudit: [id           : infoaudit.id
                                                , type       : infoaudit.type
                                                , title      : infoaudit.title
                                                , dateCreated: infoaudit.dateCreated?.format('yyyy-MM-dd HH:mm:ss ')
                                                , content    : infoaudit.content
                                                , vimTime    : infoaudit.vimTime?.format('yyyy-MM-dd HH:mm:ss ')
                                                , username   : infoaudit.publisher.name
                                                , status     : infoaudit.status.type]
            ])
        }
    }

    def update() {
        withInfoaudit(params.long('id')) { infoauditInstance ->
            println request.JSON.vimTime.class
            println request.JSON
            request.JSON.remove('vimTime')
            request.JSON.remove('dateCreated')
            infoauditInstance.type = request.JSON.type
            infoauditInstance.title = request.JSON.title
            infoauditInstance.content = request.JSON.content
            infoauditInstance.vimTime = new Date()
            infoauditInstance.save(flush: true, failOnError: true)
            renderSuccess()
        }
    }

    def delete() {
        withInfoaudit(params.long('id')) { infoauditInstance ->
            infoauditInstance.delete(flush: true)
            renderSuccess()
        }
    }

    def search() {
        renderSuccessesWithMap([publishList:infoauditService.getSearchList(request.JSON.textTitle,
                request.JSON.dateBegin, request.JSON.dateEnd, request.JSON.max, request.JSON.offset)])
    }

    def select() {
        renderSuccessesWithMap([publishList:infoauditService.getSearchListN(request.JSON.type, request.JSON.max, request.JSON.offset)])

    }


    private withInfoaudit(Long id, Closure c) {
        print id
        Infoaudit infoauditInstance = id ? Infoaudit.get(id) : null
        if (infoauditInstance) {
            c.call infoauditInstance
        } else {
            renderNoTFoundError()
        }
    }

}

