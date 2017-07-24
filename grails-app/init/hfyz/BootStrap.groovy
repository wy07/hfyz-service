package hfyz

import grails.core.GrailsApplication
import com.commons.support.CustomKafkaConsumer

class BootStrap {
    def initService
    GrailsApplication grailsApplication

    def init = { servletContext ->
        initService.initData()

        String zooKeeper = grailsApplication.config.zookeeper.ip
        String groupId = grailsApplication.config.group.id
        String topic = grailsApplication.config.kafka.consumer.topic
        def partitions = grailsApplication.config.kafka.consumer.partitions as int

        CustomKafkaConsumer example = new CustomKafkaConsumer(zooKeeper, groupId, topic)
        example.run(partitions)
    }
    def destroy = {
    }
}



