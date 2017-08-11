package com.hfyz.security

import com.commons.utils.SQLHelper
import com.hfyz.support.Organization
import com.hfyz.support.SystemCode
import grails.transaction.Transactional

@Transactional
class SupportService {

    def dataSource
    def grailsApplication

    def getOrgList(){
        def orgList = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_ORG_SQL)
        }
        formatOrgList(orgList, 0)
    }

    def getChildrenOrgs(){
        return Organization.findAllByParentIsNotNull()
    }

    def getOrgs(){
        return Organization.list([sort: 'id', order: 'desc'])
    }

    def formatOrgList(def data, def pid) {
        def result = []
        def list = data.findAll { obj -> obj.parentId == pid }
        list?.each { obj ->
            Map tMap = [:]
            tMap = [data:[ id: obj.id
                          ,name :obj.name
                          ,codeNum:obj.codeNum
                          ,expanded: true]]
            def tFind = data.find { a -> a.parentId == obj.id }

            if (tFind) {
                tMap.children = formatOrgList(data, obj.id)
            }
            result << tMap
        }
        return result
    }

    def getOrgForSelect(User user){
        def orgs
        if(grailsApplication.config.getProperty("user.rootRole.name") in user.authorities.authority){
            orgs=getOrgs()
        }else{
            orgs=user.org?[user.org]:[]
        }

        orgs?.collect{Organization org->
            [value:org.id,label:org.name]
        }

//        println roles
//        def GET_ORG_ROLES_SQL
//        if(roles){
//            def roleList=Role.findAllById(roles.split(','))
//            if (roleList[0].name=='平台管理员'){
//                GET_ORG_ROLES_SQL="select id as value,name as label from organization where parent_id is not null order by id"
//            }else{
//                GET_ORG_ROLES_SQL="select id as value,name as label from organization org left join  role_organization roleorg on org.id=roleorg.organization_id where roleorg.role_orgs_id in (${roles})"
//            }
//        }else{
//            GET_ORG_ROLES_SQL = "select id as value,name as label from organization where parent_id is not null order by id"
//        }
//        println GET_ORG_ROLES_SQL
//
//        return SQLHelper.withDataSource(dataSource) { sql ->
//            sql.rows(GET_ORG_ROLES_SQL.toString())
//        }
    }

    def getMenu() {
        def result = [:]
        def GET_MENU_SQL = "select id,name,code ,style, icon,position,permission_code from menu where parent_id is null and display=true and position in ('TOP_BAR','SIDE_BAR') order by id"
        //println GET_USER_SQL
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(GET_MENU_SQL.toString())
        }.each { root ->
            if (!result["${root.position}"]) {
                result["${root.position}"] = []
            }
            def menu = root
            def children = SQLHelper.withDataSource(dataSource) { menusql -> menusql.rows(SYSTEM_CODE_LIST_BY_MENU.toString(), [parentId: root.id])
            }
            if (children.size() > 0) {
                menu['children'] = children

            }
            result["${root.position}"] << menu
        }
        result["ROLE_RIGHTS"]=null
        return result
    }

    def getSystemCodeListByParent(Long parentId, String type) {
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(SYSTEM_CODE_LIST_BY_PARENT_AND_TYPE, [parentId: parentId, type: type])
        }?.collect { obj ->
            [data  : [id       : obj.id
                      , name   : obj.name
                      , codeNum: obj.codeNum]
             , leaf: !obj.hasChildren]
        }
    }

    def getMenuTypeListByParent(Long parentId) {
        SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(MENU_LIST_BY_PARENT_AND_TYPE, [parentId: parentId])
        }?.collect { obj ->
            [data  : [id        : obj.id
                      , code    : obj.code
                      , icon    : obj.icon
                      , name    : obj.name
                      , style   : obj.style
                      , position: obj.position
                      , display : obj.display]
             , leaf: !obj.hasChildren]
        }
    }

    def getSystemCodeListAndCountByType(int max, int offset, String type) {
        def parent = type ? SystemCode.findByParentIsNullAndType(type) : null

        def systemCodeList = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(SYSTEM_CODE_LIST_BY_TOP_PARENT, [max: max, offset: offset, parentId: parent?.id])
        }
        def systemCodeCount = parent ? SystemCode.countByParent(parent) : SystemCode.countByParentIsNull()
        [systemCodeTree   : formatSystemCodeList(systemCodeList, parent?.id ?: 0)
         , systemCodeCount: systemCodeCount
         , systemCodeTop  : parent]

    }

    def getAllSystemCodeList() {
        def systemCodeList = SQLHelper.withDataSource(dataSource) { sql ->
            sql.rows(ALL_SYSTEM_CODE_LIST_SQL)
        }
        formatSystemCodeList(systemCodeList, 0)
    }

    def formatSystemCodeList(def data, def pid) {

        def result = []
        def list = data.findAll { obj -> obj.parentId == pid }

        list?.each { obj ->
            Map tMap = [:]
            tMap = [data: [id       : obj.id
                           , name   : obj.name
                           , codeNum: obj.codeNum
                           , type   : obj.type]]
            def tFind = data.find { a -> a.parentId == obj.id }

            if (tFind) {
                tMap.children = formatSystemCodeList(data, obj.id)

            }
            result << tMap
        }
        return result
    }
    private static final SYSTEM_CODE_LIST_BY_MENU = """
        select id,name,code ,style, icon,display,permission_code from menu where display=true and parent_id=:parentId order by id
    """
    private static final SYSTEM_CODE_LIST_BY_PARENT_AND_TYPE = """
        select sc.id
            ,sc.name
            ,sc.code_num codeNum
            ,sc.type
            ,EXISTS (
               SELECT 1
               FROM   system_code sc_children
               WHERE  sc_children.parent_id = sc.id
               ) hasChildren
        from system_code sc
        where ((:parentId::text is null and sc.parent_id is null)  or (:parentId::text is not null and sc.parent_id=:parentId))
              and sc.type=:type
    """

    private static final MENU_LIST_BY_PARENT_AND_TYPE = """
        select m.id
            ,m.code
            ,m.icon
            ,m.name
            ,m.style
            ,m.position
            ,m.display
            ,EXISTS (
                SELECT 1
                FROM   menu m_children
                WHERE  m_children.parent_id = m.id
            ) hasChildren
        from menu m
        where ((:parentId::text is null and m.parent_id is null)  or (:parentId::text is not null and m.parent_id=:parentId))
    """

    private static final SYSTEM_CODE_LIST_BY_TOP_PARENT = """
        with RECURSIVE withSystemCode as(
            select * from prants
            union all
            select k.id
                ,k.name
                ,k.code_num codeNum
                ,k.type
                ,k.parent_id parentId
                ,c.depth + 1 AS depth
                ,(c.path || k.id ) as path
            from system_code k
            inner join withSystemCode c
            on c.id = k.parent_id
        )
        ,prants as(
            select a.id
                ,a.name
                ,a.code_num codeNum
                ,a.type
                ,a.parent_id parentId
                ,1 AS depth
                ,ARRAY[a.id] AS path
            from system_code a
            where (:parentId::text is null and parent_id is null)  or (:parentId::text is not null and parent_id=:parentId)
            order by id desc
            limit :max offset :offset
        )
        select id,name,codeNum,type,COALESCE(parentId,0) parentId,depth,path
        from withSystemCode
        order by path
    """

    private static final ALL_SYSTEM_CODE_LIST_SQL = """
       with RECURSIVE withSystemCode as(
            select a.id
                ,a.name
                ,a.code_num codeNum
                ,a.type
                ,a.parent_id parentId
                ,1 AS depth
                ,ARRAY[a.id] AS path
            from system_code a where parent_id is null
            union all
            select k.id
                ,k.name
                ,k.code_num codeNum
                ,k.type
                ,k.parent_id parentId
                ,c.depth + 1 AS depth
                ,(c.path || k.id ) as path
            from system_code k
            inner join withSystemCode c
            on c.id = k.parent_id
        )
        select id,name,codeNum,type,COALESCE(parentId,0) parentId,depth,path   from withSystemCode where type='SYSTEMCODE' order by path;
    """

    private static final GET_ORG_SQL = """
       with RECURSIVE withOrg as(
            select a.id
                ,a.name
                ,a.code codeNum
                ,a.parent_id parentId
                ,1 AS depth
                ,ARRAY[a.id] AS path
            from organization a where parent_id is null
            union all
            select k.id
                ,k.name
                ,k.code codeNum
                ,k.parent_id parentId
                ,c.depth + 1 AS depth
                ,(c.path || k.id ) as path
            from organization k
            inner join withOrg c
            on c.id = k.parent_id
        )
        select id,name,codeNum,COALESCE(parentId,0) parentId,depth,path   from withOrg order by path;
    """
}
