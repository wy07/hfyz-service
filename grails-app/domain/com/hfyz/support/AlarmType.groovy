package com.hfyz.support

class AlarmType extends SystemCode{
    AlarmType parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'ALARM_TYPE'
    }
}
