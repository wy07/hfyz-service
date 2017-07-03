package com.hfyz.security

import grails.transaction.Transactional
import com.commons.utils.SQLHelper
@Transactional
class RoleService {

    def dataSource
    def getUserList(operatorId){
        def result=[]
        def GET_OPERATOR_SQL = "select name from role left join user_role urole on role.id=urole.role_id where urole.user_id=:operatorId"
        println GET_OPERATOR_SQL
        def myRole=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_OPERATOR_SQL.toString(),[operatorId:operatorId])
        }
        def userList 
        if(myRole.name.contains('平台管理员')){
             //平台管理员可以看到所有用户
            userList=User.list(sort: "id")
        }else{
            //非平台管理员只能看到自己创建的用户
            userList=User.findAllByOperator(operatorId,[sort: "id"])
        }
       
        userList.each{
            result << [id:it.id
                        ,name:it.name
                        ,username:it.username
                        ,roles:it.authorities.name.join('；')]
        }
        return result
    }
    def getRoleList(operatorId){

        def result=[]
        def GET_OPERATOR_SQL = "select name from role left join user_role urole on role.id=urole.role_id where urole.user_id=:operatorId"
        println GET_OPERATOR_SQL
        def myRole=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_OPERATOR_SQL.toString(),[operatorId:operatorId])
        }
        def roleList 
        if(myRole.name.contains('平台管理员')){
            roleList=Role.list(sort: "id")
        }else{
            roleList=Role.findAllByOperator(operatorId,[sort: "id"])
        }
        println roleList
        roleList.each{
            result << [id:it.id
                    ,name:it.name
                    ,rights:it.permissionGroups.collect{it.menu?"${it.name}${it.menu.name}":it.name}
                    ,users:UserRole.findAllByRole(it).user.name
                    ,orgs:it.orgs.name]
        }
        return result
    }
    def getRoleListForSelect(roles,operatorId){
        println roles
        def result = []
        def GET_OPERATOR_SQL = "select name from role left join user_role urole on role.id=urole.role_id where urole.user_id=:operatorId"
        println GET_OPERATOR_SQL
        def myRole=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_OPERATOR_SQL.toString(),[operatorId:operatorId])
        }
        def roleList 
        def GET_ROLE_SQL
        if(myRole.name.contains('平台管理员')){
            GET_ROLE_SQL = "select id as value,name as label from role  order by id"
        }else{
            GET_ROLE_SQL = "select id as value,name as label from role where role.operator=${operatorId}   order by id"
        }
        result=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_ROLE_SQL.toString())
        }
        return result
    }
    def getMenu(roles){
        println roles
        def result=[:]
        def myRole=Role.findAllById(roles.split(','))
        def queryStr=""
        if(!myRole.name.contains('平台管理员')){

            queryStr="exists (select permission_id from role_permission where role_id in (${roles}) and permission_id=per.id) and"
        }
        def GET_MENU_SQL="select menu.name as menu,menu.id as menuid,menu.code as menucode,menu.code|| ':' || per.permissions as rights,per.name ,per.id from permission_group as per left join menu on per.menu_id=menu.id where ${queryStr} per.permissions <> '*:*' order by menu.id"
        println GET_MENU_SQL
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_MENU_SQL.toString())
        }.each { per ->
            if(!result["${per.menu}${per.menuid}"]){
                result["${per.menu}${per.menuid}"]=[label:per.menu,
                                                    data:per.menuid,
                                                    rights:"${per.menucode}:*",
                                                    //expandedIcon: "fa-folder-open",
                                                    //collapsedIcon: "fa-folder",
                                                    expanded: true,
                                                    //leaf: false,
                                                    children:[]]
            }
            result["${per.menu}${per.menuid}"].children << [label: per.name,
                                                            data: per.id,
                                                            rights:per.rights,
                                                            //collapsedIcon: "fa-leaf",
                                                            //leaf: true
                                                            ]
            
        }
        return result.values()
    }
    def getPermissionRoot(menu){
        def query=menu.collect {"'$it'"}.join(',')
        def GET_PERMISSION_SQL="select name from menu where code in (${query})"
        println GET_PERMISSION_SQL
        return SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_PERMISSION_SQL.toString())
        }
    }
    def getPermission(menuItem){
        def query=menuItem.collect {"'$it'"}.join(',')
        def GET_PERMISSION_SQL="with permissions as (select menu.name as menu,menu.id as menuid,menu.code as menucode,menu.code|| ':' || per.rights as rights,per.name ,per.id from permission as per left join menu on per.menu_id=menu.id) select * from permissions where rights in (${query})"
        println GET_PERMISSION_SQL
        return SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_PERMISSION_SQL.toString())
        }
    }
    
}