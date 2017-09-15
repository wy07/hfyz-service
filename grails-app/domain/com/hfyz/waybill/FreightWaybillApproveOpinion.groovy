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

    static mapping = {
        comment '危货电子路单审批表'
        id generator: 'native', params: [sequence: 'freight_waybill_approve_opinion_seq'], defaultValue: "nextval('freight_waybill_approve_opinion_seq')"
        freightWaybill comment: '被审批的电子路单'
        approver comment: '审批人'
        approveTime comment: '审批时间'
        approveDesc comment: '审批意见'
    }
}
