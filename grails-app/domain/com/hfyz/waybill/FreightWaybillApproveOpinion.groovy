package com.hfyz.waybill

import com.hfyz.security.User

/**
 * Created by seraphim on 2017/9/4.
 */
class FreightWaybillApproveOpinion {
    FreightWaybill freightWaybill
    User approver
    Date approveTime
    String approveDesc
}
