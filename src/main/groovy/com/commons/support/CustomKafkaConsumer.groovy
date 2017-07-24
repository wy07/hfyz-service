package com.commons.support

import com.commons.utils.KafkaDataUtils
import com.commons.utils.LogUtils
import groovy.json.JsonSlurper

//import com.commons.utils.ConsumerTest
import kafka.consumer.ConsumerConfig
import kafka.consumer.ConsumerIterator
import kafka.consumer.KafkaStream
import kafka.javaapi.consumer.ConsumerConnector

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class CustomKafkaConsumer {
    private final ConsumerConnector consumer
    private final String topic
    private  ExecutorService executor

    CustomKafkaConsumer(String zookeeper, String groupId, String topic) {
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
                createConsumerConfig(zookeeper, groupId))
        this.topic = topic
    }

    void shutdown() {
        if (consumer != null) consumer.shutdown()
        if (executor != null) executor.shutdown()
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                println "Timed out waiting for consumer threads to shut down, exiting uncleanly"
            }
        } catch (InterruptedException e) {
            println "Interrupted during shutdown, exiting uncleanly"
        }
    }

    void run(int numThreads, String topicName) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>()
        topicCountMap.put(topic, new Integer(numThreads))
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap)
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic)

        // now launch all the threads
        executor = Executors.newFixedThreadPool(numThreads)

        // now create an object to consume the messages
        int threadNumber = 0
        for (final KafkaStream stream : streams) {
            executor.submit(new Consumer(stream, threadNumber, topicName))
            threadNumber++
        }
    }

    private static ConsumerConfig createConsumerConfig(String zookeeper, String groupId) {
        Properties props = new Properties()
        props.put("zookeeper.connect", zookeeper)
        props.put("group.id", groupId)
        props.put("zookeeper.session.timeout.ms", "400")
        props.put("zookeeper.sync.time.ms", "200")
        props.put("auto.commit.interval.ms", "1000")

        return new ConsumerConfig(props)
    }
}

class Consumer implements Runnable {
    private KafkaStream m_stream
    private int m_threadNumber
    private String m_topicName

    Consumer(KafkaStream stream, int threadNumber, String topicName) {
        m_threadNumber = threadNumber
        m_stream = stream
        m_topicName = topicName
    }

    void run() {
        ConsumerIterator<byte[], byte[]> consumerMap = m_stream.iterator()
        while (consumerMap.hasNext()) {
            def data = new String(consumerMap.next().message())
            try{
                KafkaDataUtils.set(m_topicName, data)
                LogUtils.debug(this.class, KafkaDataUtils.get(m_topicName).toString())
                LogUtils.debug(this.class, data)
            }catch (Exception e){
                LogUtils.debug(this.class, e.message)
            }
        }
    }
}