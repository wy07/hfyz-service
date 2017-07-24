package hfyz

import grails.core.GrailsApplication

class BootStrap {
    def initService
    GrailsApplication grailsApplication

    def init = { servletContext ->
        initService.initData()

//        String zooKeeper = grailsApplication.config.zookeeper.ip
//        String groupId = grailsApplication.config.group.id
//        String topicLocation = grailsApplication.config.kafka.consumer.topic.location
//        def partitionsLocation = grailsApplication.config.kafka.consumer.partitions.location as int
//        String topicWarn = grailsApplication.config.kafka.consumer.topic.warn
//        def partitionsWarn = grailsApplication.config.kafka.consumer.partitions.warn as int
//
//        try{
//            CustomKafkaConsumer locationConsumer = new CustomKafkaConsumer(zooKeeper, groupId, topicLocation)
//            locationConsumer.run(partitionsLocation,'location')
//
//            CustomKafkaConsumer warnConsumer = new CustomKafkaConsumer(zooKeeper, groupId, topicWarn)
//            warnConsumer.run(partitionsWarn,'warn')
//        }catch (Exception e){
//            LogUtils.debug(this.class, e.message)
//        }
    }
    def destroy = {
    }
}



