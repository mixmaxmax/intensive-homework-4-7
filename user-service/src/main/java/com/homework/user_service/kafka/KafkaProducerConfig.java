package com.homework.user_service.kafka;

import com.homework.user_service.dto.UserEventDto;
import com.homework.user_service.entity.User;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, UserEventDto> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> configProperties = new HashMap<>();
        //configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("LOCALHOST"));
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");

        JacksonJsonSerializer<UserEventDto> serializer = new JacksonJsonSerializer<>((JsonMapper) objectMapper);
        serializer.setAddTypeInfo(false);

        return new DefaultKafkaProducerFactory<>(
                configProperties,
                new StringSerializer(),
                serializer
        );
    }

    @Bean
    public KafkaTemplate<String, UserEventDto> kafkaTemplate(ProducerFactory<String, UserEventDto> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
