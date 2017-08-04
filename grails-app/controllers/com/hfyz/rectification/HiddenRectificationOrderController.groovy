package com.hfyz.rectification

import com.commons.utils.ControllerHelper
import com.commons.utils.PageUtils
import com.commons.utils.NumberUtils
import grails.converters.JSON

class HiddenRectificationOrderController implements ControllerHelper {

    def hiddenRectificationOrderService



    def list() {
        int max = PageUtils.getMax(request.JSON.max, 10, 100)
        int offset = PageUtils.getOffset(request.JSON.offset)
        renderSuccessesWithMap(hiddenRectificationOrderService.getHiddenDangerList(max,offset,request.JSON.company,
                request.JSON.startDate,request.JSON.endDate))
    }

    def save(){
        HiddenRectificationOrder hiddenDanger = new HiddenRectificationOrder(request.JSON)
        hiddenDanger.inspectionDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.inspection)
        hiddenDanger.dealineDate = new Date().parse('yyyy-MM-dd HH:mm', request.JSON.dealine)
        hiddenDanger.billNo = System.currentTimeMillis()+""+new Random().nextInt(100000).toString().padLeft(5, '0')
        hiddenDanger.status = HiddenRectificationOrderStatus.QC
        hiddenDanger.save(flush: true,failOnError: true)
        renderSuccess()

    }

    def edit(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIn ->
                renderSuccessesWithMap([hiddenRectificationOrder:[
                        id : hiddenRectificationOrderIn.id,
                        area : hiddenRectificationOrderIn.area,
                        billNo : hiddenRectificationOrderIn.billNo,
                        enterpirse : hiddenRectificationOrderIn.enterpirse,
                        examiner : hiddenRectificationOrderIn.examiner,
                        inspectionDate : hiddenRectificationOrderIn.inspectionDate.format('yyyy-MM-dd HH:mm:ss'),
                        dealineDate : hiddenRectificationOrderIn.dealineDate.format('yyyy-MM-dd HH:mm:ss'),
                        insPosition : hiddenRectificationOrderIn.insPosition,
                        insDesc : hiddenRectificationOrderIn.insDesc,
                        insQuestion : hiddenRectificationOrderIn.insQuestion,
                        proPosal : hiddenRectificationOrderIn.proPosal,
                        replyDate : hiddenRectificationOrderIn.replyDate?.format('yyyy-MM-dd HH:mm:ss'),
                        replyDesc : hiddenRectificationOrderIn.replyDesc,
                        status : hiddenRectificationOrderIn.status.type
                ]])
        }

    }

    def delete(){
        withHiddenRectificationOrder(params.long('id')) {
            hiddenDanger ->
                hiddenDanger.delete(flush: true)
                renderSuccess()
        }
    }

    def update(){
        withHiddenRectificationOrder(params.long('id')){
            hiddenRectificationOrderIns ->
                hiddenRectificationOrderIns.properties = request.JSON
                hiddenRectificationOrderIns.inspectionDate = request.JSON.inspection ? new Date()
                        .parse('yyyy-MM-dd HH:mm', request.JSON.inspection) : null
                hiddenRectificationOrderIns.dealineDate = request.JSON.dealine ? new Date()
                        .parse('yyyy-MM-dd HH:mm', request.JSON.dealine) : null
                hiddenRectificationOrderIns.status = HiddenRectificationOrderStatus.QC
                hiddenRectificationOrderIns.save(flush: true,failOnError: true)

                renderSuccess()
        }
    }

    private withHiddenRectificationOrder(Long id,Closure c){
        HiddenRectificationOrder hiddenRectificationOrder = id ? HiddenRectificationOrder.get(id) : null
        if(hiddenRectificationOrder){
            c.call(hiddenRectificationOrder)
        }else{
            renderNoTFoundError()
        }
    }
}

