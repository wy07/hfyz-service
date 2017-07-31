package com.hfyz.rectification

import com.commons.utils.ControllerHelper

class HiddenDangerController implements ControllerHelper {

    def hiddenDangerService

    def list() {
        renderSuccessesWithMap([hiddenDangerList:hiddenDangerService.getHiddenDangerList(request.JSON.max,request.JSON.offset)])
    }

    def save(){
        HiddenDanger hiddenDanger = new HiddenDanger(request.JSON)
        hiddenDanger.save(flush: true,failOnError: true)
        renderSuccess()

    }

    def edit(){
        withHiddenDanger(params.long('id')){
            hiddenDanger ->
                renderSuccessesWithMap([
                        id : hiddenDanger.id,
                        billNo : hiddenDanger.billNo,
                        enterpirse : hiddenDanger.enterpirse,
                        examiner : hiddenDanger.examiner,
                        inspectionDate : hiddenDanger.inspectionDate,
                        dealineDate : hiddenDanger.dealineDate,
                        insPosition : hiddenDanger.insPosition,
                        insDesc : hiddenDanger.insDesc,
                        insQuestion : hiddenDanger.insQuestion,
                        proPosal : hiddenDanger.proPosal
                ])
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
