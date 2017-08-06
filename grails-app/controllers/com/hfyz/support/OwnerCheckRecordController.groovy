package com.hfyz.support
import com.commons.utils.ControllerHelper

class OwnerCheckRecordController implements ControllerHelper{
    def ownerCheckRecordService
    def index() { }

    def list() {

        def  checkResult= ownerCheckRecordService.list(request.JSON.max, request.JSON.offset, request.JSON.company,
                                request.JSON.startDate, request.JSON.endDate)
        renderSuccessesWithMap([checkResult: checkResult])

    }

    def inspect(){

        withOwnerCheckRecord(params.long('id')) {OwnerCheckRecord record ->
            if(record.responsed){
                renderErrorMsg('查岗已响应，请勿重复回答')
                return false
            }

            Date current=new Date()
            int responseTime=(current.time-record.dateCreated.time)/1000

            if(responseTime>310){
                renderErrorMsg('查岗超时')
                return false
            }

            def answer=request.JSON.answer?.trim()
            if(answer!=record.answer){
                renderErrorMsg('查岗答案错误')
                return false
            }

            record.responseDate=current
            record.responseContent=answer
            record.responsed=true
            record.responseTime=responseTime
            record.save(flush: true, failOnError: true)
            renderSuccess()
        }
    }

    private withOwnerCheckRecord(Long id, Closure c) {
        OwnerCheckRecord record = id ? OwnerCheckRecord.get(id) : null

        if (record) {
            c.call record
        } else {
            renderNoTFoundError()
        }
    }
}
