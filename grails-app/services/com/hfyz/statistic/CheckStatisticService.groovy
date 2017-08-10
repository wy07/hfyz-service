package com.hfyz.statistic

import grails.transaction.Transactional
import com.commons.utils.SQLHelper
import grails.async.Promise
import static grails.async.Promises.task

@Transactional
class CheckStatisticService {

    def dataSource

    private getCheckStatisticList(def max, def offset,def startDate,def endDate,def company,def dateLength){
        def sqlParams = [:]
        def sd = startDate
        def ed = endDate
        if(company){
            sqlParams.company = "${company}%".toString()
        }
        if(startDate){
            sqlParams.sd = sd
        }
        if(endDate){
            sqlParams.ed = ed
        }
        Promise checkStatisticList = task {
            SQLHelper.withDataSource(dataSource){ sql ->
                    sql.rows(getCheckStatisticListSql(startDate,endDate,company,dateLength),sqlParams + [max: max, offset: offset])
            }?.collect {
                obj ->
                    [ownerName : obj.enterprise,
                    responsed : obj.responsed?obj.responsed:0,
                    noResponse : obj.noresponse,
                    responseRate : new BigDecimal((obj.responsed ?obj.responsed:0)/obj.total).setScale(2,BigDecimal.ROUND_HALF_UP)*100+'%']
            }
        }

        Promise total = task{
            SQLHelper.withDataSource(dataSource){
                sql ->
                    sql.firstRow(getCheckStatisticListTotalSql(startDate,endDate,company,dateLength),sqlParams).count ?:0
            }
        }

        return [checkStatisticList:checkStatisticList.get(),total:total.get()]
    }

    private static getCheckStatisticListSql(def startDate,def endDate,def company,def dateLength){
        String strSql = """
                    select
                    count(t1.company_code) as total , 
                    (select 
                         t2.owner_name
                    from 
                         owner_basicinfo_owneridentity t2,
                         owner_check_record t
                    where 
                         t2.company_code = t1.company_code
                    group by 
                         t2.owner_name
                        ) as enterprise,
                        (select 
                                count(t.company_code) 
                        from 
                                owner_check_record t 
                        where  
                                t.responsed = true 
                        and     t.company_code = t1.company_code
                        group by 
                                t.company_code
                        ) as responsed,
                        (select 
                                count(t.company_code) 
                        from 
                                owner_check_record t 
                        where  
                                t.responsed = false 
                        and     t.company_code = t1.company_code
                        group by 
                                t.company_code
                        ) as noresponse
                from 
                    owner_check_record t1 ,
                    owner_basicinfo_owneridentity t3
                    where 
                        t3.company_code= t1.company_code
                    """
        if(startDate){
            strSql += " and t1.date_created >= :sd::timestamp"
        }
        if(endDate){
            if(dateLength == 3) {
                strSql += " and t1.date_created <= :ed::timestamp + '1 day'"
            }
            if(dateLength == 2) {
                strSql += " and t1.date_created <=  :ed::timestamp + '1 month'"
            }
            if(dateLength != 3 && dateLength != 2 && dateLength != 1){
                strSql += " and t1.date_created <= :ed::timestamp + '1 year'"
            }
        }
        if(company){
            strSql += " and t3.owner_name like :company "
        }
        strSql += """
                group by t1.company_code 
                limit :max offset :offset;
                    """
        return  strSql
    }

    private static getCheckStatisticListTotalSql(def startDate,def endDate,def company,def dateLength){
        String strSql = """
                select count(*) 
                from 
                    ( select count(t1.company_code) as total , 
                        (select 
                             t2.owner_name
                        from 
                             owner_basicinfo_owneridentity t2,
                             owner_check_record t
                        where 
                             t2.company_code = t1.company_code
                        group by 
                             t2.owner_name
                            ) as enterprise,
                            (select 
                                    count(t.company_code) 
                            from 
                                    owner_check_record t 
                            where  
                                    t.responsed = true 
                            and     t.company_code = t1.company_code
                            group by 
                                    t.company_code
                            ) as responsed,
                            (select 
                                    count(t.company_code) 
                            from 
                                    owner_check_record t 
                            where  
                                    t.responsed = false 
                            and     t.company_code = t1.company_code
                            group by 
                                    t.company_code
                            ) as noresponse
                    from 
                        owner_check_record t1 ,
                        owner_basicinfo_owneridentity t3
                        where 
                             t3.company_code= t1.company_code
                    """
        if(startDate){
            strSql += " and t1.date_created >=  :sd::timestamp"
        }
        if(endDate){
            if(dateLength == 3) {
                strSql += " and t1.date_created <= :ed::timestamp + '1 day'"
            }
            if(dateLength == 2) {
                strSql += " and t1.date_created <= :ed::timestamp + '1 month'"
            }
            if(dateLength != 3 && dateLength != 2 && dateLength != 1){
                strSql += " and t1.date_created <= :ed::timestamp + '1 year'"
            }
        }
        if(company){
            strSql += " and t3.owner_name like :company "
        }
        strSql += """
                group by t1.company_code) view1
                   """
        return strSql
    }
}
