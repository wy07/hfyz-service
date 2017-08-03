package com.hfyz.security

import org.springframework.http.HttpMethod

class PermissionGroup {

    String category
    String name
    String code
    String configAttribute
    HttpMethod httpMethod
    String url

    static constraints = {
        category nullable: false, blank: false, maxSize: 50
        name nullable: false, blank: false, maxSize: 20
        code nullable: false, blank: false, maxSize: 50
        configAttribute blank: false, nullable: false, maxSize: 200
        httpMethod nullable: true, maxSize: 50
        url blank: false, unique: 'httpMethod', maxSize: 100
    }
}
