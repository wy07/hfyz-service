package com.hfyz

import com.commons.utils.URIUtils

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?" {
            constraints {
                // apply constraints here
            }
        }

        "/"(view: "/index")
        "500"(view: '/error')
        "404"(view: '/notFound')

        "/$resource/$actionStr" {
            controller = { URIUtils.parseResourceToControllerName(params.resource) }
            action = { new URIUtils(URIPartStr: params.actionStr).parseSeparatorToCamelCase().toString() }
            constraints {
                resource matches: /\S+s$/
                actionStr matches: /\S+[^s]$/
            }
        }

        "/$resource/" {
            controller = { URIUtils.parseResourceToControllerName(params.resource) }
            action = [GET: "list", POST: "save"]
            constraints {
                resource matches: /\S+s$/      //resource 所有的resource均需要以s结尾
            }
        }

        "/$resource/$id" {
            controller = { URIUtils.parseResourceToControllerName(params.resource) }
            action = [GET: "show", PUT: "update", POST: "update", DELETE: "delete"]
            constraints {
                resource matches: /\S+s$/
                id matches: /\d+/
            }
        }

        "/$resource/$id/$actionStr" {
            controller = { URIUtils.parseResourceToControllerName(params.resource) }
            action = { new URIUtils(URIPartStr: params.actionStr).parseSeparatorToCamelCase().toString() }
            constraints {
                resource matches: /\S+s$/
                id matches: /\d+/
                actionStr matches: /\S+[^s]$/        //不以s结尾的单词,和普通的resource区分开
            }
        }

//        //SystemCode
//        "/system-codes/$className/$actionStr" {
//            controller = 'systemCode'
//            action = { new URIUtils(URIPartStr: params.actionStr).parseSeparatorToCamelCase().toString() }
//            constraints {
//                className matches: /(department|unit|license-type|unit-nature)/
//                actionStr matches: /\S+[^s]$/
//            }
//        }
//
//        "/system-codes/$className" {
//            controller = 'systemCode'
//            action = [GET: "list", POST: "save"]
//            constraints {
//                className matches: /(department|unit|license-type|unit-nature)/
//            }
//        }
//
//        "/system-codes/$className/$id" {
//            controller = 'systemCode'
//            action = [GET: "show", PUT: "update", POST: "update", DELETE: "delete"]
//            constraints {
//                className matches: /(department|unit|license-type|unit-nature)/
//                id matches: /\d+/
//            }
//        }
//
//        "/system-codes/$className/$id/$actionStr" {
//            controller = 'systemCode'
//            action = { new URIUtils(URIPartStr: params.actionStr).parseSeparatorToCamelCase().toString() }
//            constraints {
//                className matches: /(department|unit|license-type|unit-nature)/
//                id matches: /\d+/
//                actionStr matches: /\S+[^s]$/        //不以s结尾的单词,和普通的resource区分开
//            }
//        }
    }
}
