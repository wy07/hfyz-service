package com.commons.support

import kafka.consumer.Consumer
import kafka.consumer.ConsumerConfig
import kafka.consumer.ConsumerIterator
import kafka.consumer.KafkaStream
import kafka.javaapi.consumer.ConsumerConnector

class KafkaConsumer {

    private final ConsumerConnector consumer
    private final String topic

    KafkaConsumer(String zookeeper, String groupId, String topic) {
        Properties props = new Properties()
//        props.put("zookeeper.connect", zookeeper)
//        props.put("group.id", groupId)
//        props.put("zookeeper.session.timeout.ms", "500")
//        props.put("zookeeper.sync.time.ms", "250")
//        props.put("auto.commit.interval.ms", "1000")
        props.put("zookeeper.connect", zookeeper)
        props.put("group.id", groupId)
        props.put("zookeeper.session.timeout.ms", "400")
        props.put("zookeeper.sync.time.ms", "200")
        props.put("auto.commit.interval.ms", "1000")
        props.put("auto.offset.reset", "smallest")

        consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(props))
        this.topic = topic
    }

    def testConsumer() {
        Map<String, Integer> topicCount = new HashMap<>()
        topicCount.put(topic, 1)

        Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer.createMessageStreams(topicCount)
        List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic)
        for (final KafkaStream stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator()
            while (it.hasNext()) {
                println "Message from Single Topic: " + new String(it.next().message())
            }
        }
        if (consumer != null) {
            consumer.shutdown()
        }
    }

}