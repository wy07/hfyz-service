package com.commons.support

import com.commons.utils.LogUtils

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

    void run(int numThreads) {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>()
        topicCountMap.put(topic, new Integer(numThreads))
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap)
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topic)

        // now launch all the threads
        executor = Executors.newFixedThreadPool(numThreads)

        // now create an object to consume the messages
        int threadNumber = 0
        for (final KafkaStream stream : streams) {
            executor.submit(new Consumer(stream, threadNumber))
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

//class Consumer implements Runnable {
//    private KafkaStream stream
//    private int threadNumber
//
//    Consumer(KafkaStream stream, int threadNumber) {
//        threadNumber = threadNumber
//        stream = stream
//    }
//
//    void run() {
//        ConsumerIterator<byte[], byte[]> it = stream.iterator()
//        while (it.hasNext()){
//            println "Thread " + threadNumber + ": " + new String(it.next().message())
//        }
//        println "Shutting down Thread: " + threadNumber
//    }
//}

class Consumer implements Runnable {
    private KafkaStream m_stream
    private int m_threadNumber

    Consumer(KafkaStream stream, int threadNumber) {
        m_threadNumber = threadNumber
        m_stream = stream
    }

    void run() {
        ConsumerIterator<byte[], byte[]> it = m_stream.iterator()
        while (it.hasNext()){
            LogUtils.debug(this.class, new String(it.next().message()))
//            println "Thread " + m_threadNumber + ": " + new String(it.next().message())
        }
        println "Shutting down Thread: " + m_threadNumber
    }
}