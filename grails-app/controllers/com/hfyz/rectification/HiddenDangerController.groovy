package com.hfyz.rectification

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.commons.utils.NumberUtils
import grails.converters.JSON

class HiddenDangerController implements ControllerHelper {

    def hiddenDangerService



    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(hiddenDangerService.getHiddenDangerList(max,offset,request.JSON.company,
                request.JSON.startDate,request.JSON.endDate))
    }

    def save(){
        HiddenDanger hiddenDanger = new HiddenDanger(request.JSON)
        hiddenDanger.inspectionDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.inspection)
        hiddenDanger.dealineDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.dealine)
        hiddenDanger.billNo = System.currentTimeMillis()+""+new Random().nextInt(100000).toString().padLeft(5, '0')
        hiddenDanger.save(flush: true,failOnError: true)
        renderSuccess()

    }

    def edit(){
        withHiddenDanger(params.long('id')){
            hiddenDanger ->
                renderSuccessesWithMap([hiddenDanger:[
                        id : hiddenDanger.id,
                        area : hiddenDanger.area,
                        billNo : hiddenDanger.billNo,
                        enterpirse : hiddenDanger.enterpirse,
                        examiner : hiddenDanger.examiner,
                        inspectionDate : hiddenDanger.inspectionDate.format('yyyy-MM-dd HH:mm:ss'),
                        dealineDate : hiddenDanger.dealineDate.format('yyyy-MM-dd HH:mm:ss'),
                        insPosition : hiddenDanger.insPosition,
                        insDesc : hiddenDanger.insDesc,
                        insQuestion : hiddenDanger.insQuestion,
                        proPosal : hiddenDanger.proPosal,
                        replyDate : hiddenDanger.replyDate?.format('yyyy-MM-dd HH:mm:ss'),
                        replyDesc : hiddenDanger.replyDesc,
                        status : hiddenDanger.status
                ]])
        }

    }



    def delete(){
        withHiddenDanger(params.long('id')) {
            hiddenDanger ->
                hiddenDanger.delete(flush: true)
                renderSuccess()
        }
    }

    def update(){
        withHiddenDanger(params.long('id')){
            hiddenDangerIns ->
                hiddenDangerIns.properties = request.JSON
                hiddenDangerIns.inspectionDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.inspection)
                hiddenDangerIns.dealineDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.dealine)
                if(request.JSON.reply){
                    hiddenDangerIns.replyDate = new Date().parse('yyyy-MM-dd HH:mm',request.JSON.reply)
                }
                hiddenDangerIns.save(flush: true,failOnError: true)
                renderSuccess()
        }
    }

    private withHiddenDanger(Long id,Closure c){
        HiddenDanger hiddenDanger = id ? HiddenDanger.get(id) : null
        if(hiddenDanger){
            c.call(hiddenDanger)
        }else{
            renderNoTFoundError()
        }
    }
}
