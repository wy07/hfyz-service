package com.commons.utils

import groovy.json.JsonSlurper

/**
 * Created by seraphim on 2017/7/18.
 */
class KafkaDataUtils {

    public static Map dataList = [:]

    static create(String dataName) {
        dataList["${dataName}"]=[:]
    }


    static create(List nameList) {
        nameList.each {dataName ->
            dataList["${dataName}"]=[:]
        }
    }

    static set(dataName, data){
        def map = [:]
        def jsonSlurper = new JsonSlurper()
        def newData = jsonSlurper.parseText(data)
        if(dataName == 'location'){
            map[newData?.plateNo] = data
        }
        dataList[dataName] << map
    }

    static get(dataName) {
        dataList[dataName]
    }
}
