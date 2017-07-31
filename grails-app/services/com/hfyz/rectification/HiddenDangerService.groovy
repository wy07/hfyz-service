package com.hfyz.rectification

import grails.transaction.Transactional

@Transactional
class HiddenDangerService {

    def getHiddenDangerList(def max, def offset) {
        def total = HiddenDanger.count()
        def hiddenDangerList = HiddenDanger.createCriteria().list([max:max,offset:offset])?.collect{
            HiddenDanger it ->
                [
                        id : it.id,
                        billNo : it.billNo,
                        enterpirse : it.enterpirse,
                        examiner : it.examiner,
                        inspectionDate : it.inspectionDate,
                        dealineDate : it.dealineDate,
                        insPosition : it.insPosition,
                        insDesc : it.insDesc,
                        insQuestion : it.insQuestion,
                        proPosal : it.proPosal
                ]
        }

        return [hiddenDangerList:hiddenDangerList,total:total]
    }

}
