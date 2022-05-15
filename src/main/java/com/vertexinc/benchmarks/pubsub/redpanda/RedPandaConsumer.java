package com.vertexinc.benchmarks.pubsub.redpanda;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class RedPandaConsumer {
    public static void main(String[] args) {
        Properties props = new Properties(); //RedPandaClientConfig.consumerConfig();
        
        props.put("bootstrap.servers", "localhost:9092");
        
        //props.put("producer.security.protocol","SSL");
        //props.put("producer.ssl.truststore.location", "/tmp/kafka.client.truststore.jks");

        //props.put("consumer.security.protocol","SSL");
        //props.put("consumer.ssl.truststore.location","/tmp/kafka.client.truststore.jks");
        
        props.put("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT_HOST://localhost:9092");
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("group.id", "firefox");
        props.put("auto.offset.reset", "earliest");
        		
        
        KafkaConsumer<Object, Object> consumer = new KafkaConsumer<Object, Object>(props);
        consumer.subscribe(Collections.singletonList("redpanda-topic"));
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