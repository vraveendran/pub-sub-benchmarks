package com.vertexinc.benchmarks.pubsub.kafka;

import java.util.Properties;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaClientConfig {
    private static Properties baseConfig() {
        Properties props = new Properties();
        //props.put("bootstrap.servers", System.getenv("KAFKA_BROKERS"));
        props.put("bootstrap.servers", "172.21.192.1:9092");
        String schemaRegistryUrl = System.getenv("SCHEMA_REGISTRY_URL");
        props.put("schema.registry.url",
            schemaRegistryUrl != null ? schemaRegistryUrl : "");

        // SASL-SCRAM
        StringBuilder jaasConfig = new StringBuilder();
        jaasConfig.append("org.apache.kafka.common.security.scram.ScramLoginModule ");
        jaasConfig.append("required ");
        jaasConfig.append(String.format("username='%s' ", System.getenv("KAFKA_USERNAME")));
        jaasConfig.append(String.format("password='%s';", System.getenv("KAFKA_PASSWORD")));

        props.put("security.protocol", "SASL_SSL");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", jaasConfig.toString());
        
        // TLS
        // props.put("ssl.truststore.location", "");
        // props.put("ssl.truststore.password", System.getenv("KAFKA_TRUSTSTORE_PASSWORD"));
        // props.put("ssl.keystore.location", "");
        // props.put("ssl.keystore.password", System.getenv("KAFKA_KEYSTORE_PASSWORD"));

        // props.put("schema.registry.ssl.truststore.location", "");
        // props.put("schema.registry.ssl.truststore.password", System.getenv("SCHEMA_REGISTRY_TRUSTSTORE_PASSWORD"));
        // props.put("schema.registry.ssl.keystore.location", "");
        // props.put("schema.registry.ssl.keystore.password", System.getenv("SCHEMA_REGISTRY_KEYSTORE_PASSWORD"));

        return props;
    }

    public static Properties producerConfig() {
        Properties props = baseConfig();
        props.put("key.serializer", StringSerializer.class.getName());
        props.put("value.serializer", StringSerializer.class.getName());
        return props;
    }

    public static Properties consumerConfig() {
        Properties props = baseConfig();
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        props.put("group.id", "firefox");
        props.put("auto.offset.reset", "earliest");
        return props;
    }
}
