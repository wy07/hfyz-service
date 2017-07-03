package com.hfyz.security
import com.hfyz.support.Menu
class PermissionGroup {
    String name
    String permissions
    Menu menu
    static constraints = {
        name nullable:false,unique:'menu',blank:false,maxSize:20
        permissions nullable:false,unique:'menu',blank:false,maxSize:20
        menu nullable:true,blank:false
    }
}
