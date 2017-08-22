package com.commons.utils

import com.commons.exception.FileUploadException
import com.commons.exception.IllegalActionException
import com.commons.exception.InstancePermException
import com.commons.exception.ParamsIllegalException
import com.commons.exception.RecordNotFoundException
import com.hfyz.security.User
import grails.converters.JSON
import grails.validation.ValidationException
import org.springframework.validation.Errors


trait ControllerHelper {

    def springSecurityService

    User getCurrentUser() {
        long userId = getCurrentUserId()
        if (!userId) {
            return null
        }
        User.get(userId)
    }

    Long getCurrentUserId() {

        def userId = params.jwtToken?.id

        if (!userId) {
            return null
        }
        return userId as long
    }

    def renderSuccess() {
        response.setStatus(200)
        def map = [result: 'success']
        render map as JSON
    }

    def renderSuccessesWithMap(Map model) {
        response.setStatus(200)
        model += [result: 'success']
        render(model as JSON)
    }


    def renderErrorMsg(msg) {
        response.setStatus(400)
        def map = [errors: [msg]]
        render map as JSON
    }

    def renderParamsIllegalErrorMsg(msg = null) {
        response.setStatus(400)
        def map = [errors: [msg ?: '请求参数不合法，请查证！']]
        render map as JSON
    }

    def renderFileUploadErrorMsg(msg = null) {
        response.setStatus(400)
        def map = [errors: [msg ?: '请求参数不合法，请查证！']]
        render map as JSON
    }

    def renderValidationErrors(Errors errors) {
        response.setStatus(400)
        def map = [errors: errors?.allErrors.collect { message(error: it, encodeAs: 'HTML') }]
        render map as JSON
    }

    def renderNoTFoundError() {
        response.setStatus(404)
        def map = [errors: ['找不到您请求的数据，请查正！']]
        render map as JSON
    }

    def renderNoInstancePermError() {
        response.setStatus(403)
        def map = [errors: [message(code: 'default.instance.noPermission.message', default: '您没有权限进行此操作')]]
        render map as JSON
    }

    def renderIllegalActionError(){
        response.setStatus(400)
        def map = [errors: ['操作非法，请稍后再试！']]
        render map as JSON
    }

    def handleException(Exception e) {
        println e.printStackTrace()
        renderErrorMsg('系统忙，请稍后再试!')
    }

    def handleParamsIllegalException(ParamsIllegalException e) {
        renderParamsIllegalErrorMsg(e.message)
    }

    def handleRecordNotFoundException(RecordNotFoundException e) {
        renderErrorMsg('数据错误')
    }

    def handleValidationException(ValidationException e) {
        renderValidationErrors(e.errors)
    }

    def handleInstancePermException(InstancePermException e) {
        renderNoInstancePermError()
    }

    def handleIllegalActionException(IllegalActionException e) {
        renderIllegalActionError()
    }

    def handleFileUploadException(FileUploadException e){
        renderFileUploadErrorMsg(e.message)
    }
}
