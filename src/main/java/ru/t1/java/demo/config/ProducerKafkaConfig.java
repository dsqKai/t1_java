package ru.t1.java.demo.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.java.demo.kafka.client.KafkaClientProducer;
import ru.t1.java.demo.kafka.metric.KafkaMetricProducer;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.dto.TimeLimitLogDto;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProducerKafkaConfig<T> {
    @Value("${track.kafka.bootstrap-server}")
    private String bootstrapServers;

    @Value("${t1.kafka.topic.metric-trace}")
    private String metricTraceTopic;

    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;

    @Bean("client")
    @Primary
    public KafkaTemplate<String, T> kafkaClientTemplate(@Qualifier("producerClientFactory") ProducerFactory<String, T> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaClientProducer producerClient(@Qualifier("client") KafkaTemplate<String, ClientDto> template) {
        template.setDefaultTopic(clientTopic);
        return new KafkaClientProducer(template);
    }


    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaMetricProducer producerMetricTrace(@Qualifier("client") KafkaTemplate<String, TimeLimitLogDto> template) {
        template.setDefaultTopic(metricTraceTopic);
        return new KafkaMetricProducer(template);
    }


    @Bean("producerClientFactory")
    public ProducerFactory<String, T> producerClientFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return new DefaultKafkaProducerFactory<>(props);
    }
}
