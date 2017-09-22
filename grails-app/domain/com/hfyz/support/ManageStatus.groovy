package com.hfyz.support
/**
 *货运站--基本信息--经营状态
 */
class ManageStatus extends SystemCode {
    ManageStatus parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'MANAGE_STATUS'
    }
}
