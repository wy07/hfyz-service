package com.commons.utils
import groovy.sql.Sql
class SQLHelper {
    static def withDataSource(dataSource, closure) {
        def sql = null
        def result = null
        try {
            sql = new Sql(dataSource)
            result = closure(sql)
        } catch (Exception e) {
            throw e
        } finally {
            sql?.close()
        }
        return result
    }
    static def getCount(dataSource,sqlStr){
        return withDataSource(dataSource) { sql ->
            sql.rows(sqlStr)
        }
    }
}