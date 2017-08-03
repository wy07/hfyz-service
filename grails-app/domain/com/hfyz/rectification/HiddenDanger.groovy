package com.hfyz.rectification

import com.hfyz.infoManage

class HiddenDanger {

    String billNo
    String area
    String enterpirse
    String examiner
    Date   inspectionDate
    Date   dealineDate
    String insPosition
    String insDesc
    String insQuestion
    String proPosal
    Date   replyDate
    String replyDesc
    Status status


    static constraints = {
        billNo unique: true, blank: false
        area     nullable: true
        proPosal  nullable: false
        replyDate nullable: true
        replyDesc nullable: true

    }
}
