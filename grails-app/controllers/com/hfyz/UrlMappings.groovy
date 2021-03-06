package com.hfyz

import com.commons.utils.URIUtils

class UrlMappings {

    static mappings = {
//        "/$controller/$action?/$id?(.$format)?" {
//            constraints {
//                // apply constraints here
//            }
//        }

//        "/"(view: "/index")
//        "500"(view: '/error')
//        "404"(view: '/notFound')

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

        "/roles/$id/permission-groups" {
            controller = "permissionGroup"
            action = [POST: "save"]
            constraints {
                id matches: /\d+/
            }
        }

        "/login"(controller: "auth", action: 'singIn')

        "/home"(controller: 'sysuser',action: 'home')

        "/companys/$companyCode/cars"(controller: 'car',action: 'getCompanyCars')

        "/cars/infos"(controller: 'car',action: 'getCarInfo')

        "/system-code/get-dangerous-types"(controller: 'systemCode',action: 'getDangerousTypeList')

        "/people-basic-info/get-company-worker-drivers-and-manager"(controller: 'peopleBasicInfo',action: 'getCompanyWorkerDriversAndManager')

    }
}
