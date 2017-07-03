package com.hfyz.security

import com.commons.utils.SQLHelper
import grails.converters.JSON
import java.text.SimpleDateFormat
import com.commons.utils.ControllerHelper

class RoleController implements ControllerHelper{
    def dataSource
    def roleService
    def supportService
    def list() {
        renderSuccessesWithMap([roleList: roleService.getRoleList(params.int("operatorId"))])
    }
    def listForSelect(){ 
        renderSuccessesWithMap([roleList: roleService.getRoleListForSelect(params.roles,params.int("operatorId")),
                                orgList: supportService.getOrgForSelect(params.roles)])
    }
   def save(){
       println request.JSON
       Role role = new Role(request.JSON)
       role.save(flush: true, failOnError: true)
       renderSuccess()
   }
   def edit(){
       withRole(params.long('id')) { role ->
            renderSuccessesWithMap([role    : [name   : role.name
                                               , authority : role.authority
                                               , orgs: role.orgs.id
                                               , id:role.id
                                    ],
                                    orgList:supportService.getOrgForSelect(params.roles)])
        }
   }
   def update(){
        withRole(params.long('id')) { roleInstance ->
            roleInstance.properties = request.JSON
            roleInstance.save(flush: true, failOnError: true)
        }
        renderSuccess()
   }
   def delete() {
        withRole(params.long('id')) { roleInstance ->
            UserRole.removeAll(roleInstance)
            roleInstance.delete(flush: true)
            renderSuccess()
        }
    }
   private withRole(Long id, Closure c) {
        Role roleInstance = id ? Role.get(id) : null

        if (roleInstance) {
            c.call roleInstance
        } else {
            renderNoTFoundError()
        }
    }
    def getUsersByRoleId(){
        def GET_USERS_SQL="select * from sys_user sysuser where exists (select user_id from user_role ur where ur.user_id=sysuser.id and ur.role_id=:roleId)"
        def result=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_USERS_SQL.toString(), [ roleId: params.int('roleId')])
        }
        render result as JSON
    }
    def saveRole(){
        def params=request.JSON
        Role role
        if(params.id){
            role =Role.findById(params.id)
            role.name=params.name
            role.authority=params.authority
        }else{
            role =new Role(params)
        }
       
        if(role.save(flush:true)){
            def result=[msg:"success!"]
            render result as JSON
        }else{
            renderError(role)
        }
    }
    def saveUser(){
        def params=request.JSON
        //println params
        User user
        if(params.id){
            user = User.findById(params.id)
            
            if(user.password!=params.password){
                user.password=params.password
            }
            
            user.username=params.username
            user.name=params.name
            user.tel=params.tel
            user.email=params.email

            UserRole userRole= UserRole.findByUser(user)
            if(userRole && userRole.role.id != params.role){
                userRole?.delete()
                Role role=Role.findById(params.role)
                new UserRole(user:user,role:role).save()
            }
            

            if(user.save(flush:true)){
               def result=[msg:"success!"]
                render result as JSON 
            }else{
                renderError(user)
            }
        }else{
            user=new User(params)
            if(user.save(flush:true)){
                Role role=Role.findById(params.role)
                
                UserRole.create user, role
                UserRole.withSession {
                    it.flush()
                    it.clear()
                }
                def result=[msg:"success!"]
                render result as JSON
            }else{
                renderError(user)
            }
        }
    }
    def getUserByName(){
        println params
        def result
        def userlist
        def GET_USER_SQL="select suser.id,suser.date_created,suser.last_updated,suser.password,suser.name,suser.username,suser.tel,role.id role_id,role.name role_name,array(select jsonb_array_elements(suser.rights)->>'value') rights,array(select jsonb_array_elements(role.rights)->>'value') role_rights from sys_user suser, user_role ur,role where ur.user_id=suser.id and ur.role_id=role.id  and  suser.username=:username"
        //println GET_USER_SQL
        userlist=SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_USER_SQL.toString(),[username:params.name])
        }
        if(userlist?.size()>0){
            def roleRights=[]
            userlist[0].role_rights?.getArray().each{
                it.split(',').eachWithIndex{ right,index ->
                    roleRights << right
                }
            }
            result=[user:[id:userlist[0].id
                        ,name:userlist[0].name
                        ,username:userlist[0].username
                        ,password:userlist[0].password
                        ,dateCreated:userlist[0].date_created
                        ,lastUpdated:userlist[0].last_updated
                        ,tel:userlist[0].tel
                        ,rights:userlist[0].rights?.getArray()
                        ,roleId:userlist[0].role_id
                        ,roleName:userlist[0].role_name
                        ,roleRights:roleRights]]
        }else{
            result=[msg:"error!",errors:['该用户不存在！']]
        }
        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss")
        JSON.registerObjectMarshaller(java.sql.Timestamp) {o -> sdf.format (o)}
        //println result
        render result as JSON
    }
    protected void notFound() {
        def map=['result':'error','errors':['找不到该数据！']]  
        render map as JSON
    }
    def renderError= { errorInstance ->
        def map=['result':'error','errors':errorInstance.errors.allErrors.collect {
            message(error:it,encodeAs:'HTML')
        }]
        delegate.render map as JSON
    }
}
