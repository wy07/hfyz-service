package com.hfyz.rectification

import grails.transaction.Transactional

@Transactional
class HiddenDangerService {

    def getHiddenDangerList(def max, def offset) {
        def total = HiddenDanger.count()
        def hiddenDangerList = HiddenDanger.list([max:max,offset:offset])?.collect{
            HiddenDanger obj ->
                [
                        id : obj.id,
                        billNo : obj.billNo,
                        enterpirse : obj.enterpirse,
                        examiner : obj.examiner,
                        inspectionDate : obj.inspectionDate,
                        dealineDate : obj.dealineDate,
                        insPosition : obj.insPosition,
                        insDesc : obj.insDesc,
                        insQuestion : obj.insQuestion,
                        proPosal : obj.proPosal
                ]
        }

        return [hiddenDangerList:hiddenDangerList,total:total]
    }

}
