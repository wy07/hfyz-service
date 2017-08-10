package com.hibernate.usertype

import groovy.transform.CompileStatic

@CompileStatic
class JsonbMapType extends JsonMapType {

    static int SQLTYPE = 11900004

    @Override
    int[] sqlTypes() {
        SQLTYPE as int[]
    }
}
