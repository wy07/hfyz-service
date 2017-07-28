package com.hfyz.warning

import com.hfyz.support.AlarmType

class Alarm {
    AlarmType alarmType     //告警类型
    AlarmLevel alarmLevel   //告警级别
    SourceType sourceType   //告警对象类型 人员/业户/车辆
    String sourceCode       //告警对象 人员-身份证 业户-业户编码 车辆-车架号
    Date alarmTime          //告警时间
    Date updateTime         //告警更新时间
    Date dateCreated
    String note             //备注 eg：***业户营运资格还有30天过期

    static constraints = {
        alarmType nullable: false
        alarmLevel nullable: false
        sourceType nullable: false
        sourceCode nullable: false,blank: false,maxSize: 20
        alarmTime nullable: false
        updateTime nullable: false
        note nullable: true, blank: false, maxSize: 300
    }

    static mapping = {
        id generator:'native', params:[sequence:'alarm_seq'], defaultValue: "nextval('alarm_seq')"
    }

}
