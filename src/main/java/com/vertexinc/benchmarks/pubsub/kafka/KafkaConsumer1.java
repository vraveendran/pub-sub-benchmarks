package com.vertexinc.benchmarks.pubsub.kafka;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class KafkaConsumer1 {
    public static void main(String[] args) {
        Properties props = KafkaClientConfig.consumerConfig();
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<Object, Object>(props);
        consumer.subscribe(Collections.singletonList("test"));
        consumer.seekToBeginning(consumer.assignment());

        try {
            while (true) {
                ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(10));
                if (records.count() == 0) {
                    System.out.println("No more records");
                    break;
                }
                records.forEach(record -> {
                    System.out.printf("Consumed record: %s\n", record.toString());
                });
            }
        } catch(Exception e) {
            System.out.println(e);
        } finally {
            consumer.close();
        }
    }
}