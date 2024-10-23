package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.kafka.metric.KafkaMetricProducer;
import ru.t1.java.demo.model.TimeLimitExceedLog;
import ru.t1.java.demo.repository.TimeLimitExceedLogRepository;

@RequiredArgsConstructor
@Aspect
@Component
public class TimeLimitExceedAspect {
    private final TimeLimitExceedLogRepository logRepository;
    private final KafkaMetricProducer kafkaMetricProducer;

    @Value("${track.time-limit-exceed}")
    private long timeLimit;

    @Value("${track.db.enabled}")
    private boolean isDatabaseEnabled;

    @Pointcut("@annotation(Timed)")
    public void timedMethods() {
    }

    @Around("timedMethods()")
    public Object logIfExceedsTimeLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        if (executionTime > timeLimit) {
            String methodName = joinPoint.getSignature().toShortString();
            TimeLimitExceedLog logEntry = new TimeLimitExceedLog(methodName, executionTime);
            if (isDatabaseEnabled) {
                logRepository.save(logEntry);
            }
            kafkaMetricProducer.sendMessage(logEntry);
        }
        return result;
    }
}
