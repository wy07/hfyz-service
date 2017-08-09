package com.hfyz.support
/**
 * 危险品分类信息
 */
class DangerousType extends SystemCode {
    DangerousType parent

    static constraints = {
        parent nullable: true
    }

    static mapping = {
        discriminator 'DANGEROUS_TYPE'
    }
}
