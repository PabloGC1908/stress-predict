package com.pgc.stress_predict.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

public class KafkaTopicConfig {
    public NewTopic topicStress() {
        return TopicBuilder.name("predicciones-estres")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
