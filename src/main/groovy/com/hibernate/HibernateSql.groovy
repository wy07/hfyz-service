package com.hibernate

import org.hibernate.SessionFactory
import org.hibernate.transform.AliasToEntityMapResultTransformer


class HibernateSql {

    SessionFactory sessionFactory

    HibernateSql(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory
    }

    List rows(String sql, Class domainClazz){
        rowsWithClosure(sql, domainClazz, {})
    }

    List rows(String sql){
        rowsWithClosure(sql, null, {})
    }

    private List rowsWithClosure(String sql, Class domainClazz, Closure closure){
        boolean needClose = false
        def session
        try {
            session = sessionFactory.currentSession
        }catch(org.hibernate.HibernateException e){
            session = sessionFactory.openSession()
            needClose = true
        }

        def query = session.createSQLQuery(sql)
        if(domainClazz){
            query.addEntity(domainClazz)
        }else {
            query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        }
        closure(query)
        def result = query.list()
        if(needClose){
            session.close()
        }
        return result
    }

    List rows(String sql, List params){
        rowsWithClosure(sql, null){ query ->
            params.eachWithIndex{ o, idx ->
                query.setParameter(idx, o)
            }
        }
    }

    List rows(String sql, List params, Class domainClazz){
        rowsWithClosure(sql, domainClazz){ query ->
            params.eachWithIndex{ o, idx ->
                query.setParameter(idx, o)
            }
        }
    }

    List rows(String sql, Map params){
        rowsWithClosure(sql, null){ query ->
            query.setProperties(params)
        }
    }

    List rows(String sql, Map params, Class domainClazz){
        rowsWithClosure(sql, domainClazz){ query ->
            query.setProperties(params)
        }
    }

    Map firstRow(String sql){
        firstRowWithClosure(sql, null, {})
    }

    Object firstRow(String sql, Class domainClazz){
        firstRowWithClosure(sql, domainClazz, {})
    }

    private Object firstRowWithClosure(String sql, Class domainClazz, Closure closure){
        def results = rowsWithClosure(sql, domainClazz){ query ->
            closure(query)
            query.setMaxResults(1)
        }
        results.size()== 1?results[0]:[:]
    }


    Map firstRow(String sql, Map params){
        firstRowWithClosure(sql, null){ query ->
            query.setProperties(params)
        }
    }

    Object firstRow(String sql, Map params, Class domainClazz){
        firstRowWithClosure(sql, domainClazz){ query ->
            query.setProperties(params)
        }
    }

    Map firstRow(String sql, List params){
        firstRowWithClosure(sql, null){ query ->
            params.eachWithIndex{ o, idx ->
                query.setParameter(idx, o)
            }
        }
    }
    Object firstRow(String sql, List params, Class domainClazz){
        firstRowWithClosure(sql, domainClazz){ query ->
            params.eachWithIndex{ o, idx ->
                query.setParameter(idx, o)
            }
        }
    }

    Object getSingleValue(String sql){
        getSingleValueMeta(sql, [])
    }

    Object getSingleValue(String sql, List params){
        getSingleValueMeta(sql, params)
    }

    Object getSingleValue(String sql, Map params){
        getSingleValueMeta(sql, params)
    }

    private Object getSingleValueMeta(String sql, params){
        Map row = firstRow(sql, params)
        row?(row.values() as List)[0]:null
    }

}