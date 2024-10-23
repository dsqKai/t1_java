package ru.t1.java.demo.kafka.metric;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.mapper.MetricMapper;
import ru.t1.java.demo.model.TimeLimitExceedLog;
import ru.t1.java.demo.model.dto.TimeLimitLogDto;

import java.util.concurrent.ExecutionException;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaMetricProducer {
    private final KafkaTemplate<String, TimeLimitLogDto> template;

    @Autowired
    private MetricMapper mapper;

    public void sendMessage(TimeLimitExceedLog message) {
        try {
            template.sendDefault(mapper.toDto(message)).get();
            template.flush();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Failed to send message: {}", e.getMessage(), e);
        }
    }
}
