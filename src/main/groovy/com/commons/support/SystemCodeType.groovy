package com.commons.support

import com.commons.utils.URIUtils
import grails.util.Holders
import org.grails.orm.hibernate.cfg.GrailsDomainBinder

@Singleton
class SystemCodeType {

    private types=[:]

    public void initSystemCodeTypes(){
        types=[:]
        Holders.grailsApplication.domainClasses.find {
            if (it.clazz.superclass.simpleName == 'SystemCode') {
                def type=GrailsDomainBinder.getMapping(it.clazz).discriminator
                types.put(type
                        , [name:it.clazz.simpleName, clazz:it.clazz, type:type])
            }
        }
    }

    public Map getSystemCodeTypes(){
        if(!types){
            initSystemCodeTypes()
        }
        return types
    }
}
