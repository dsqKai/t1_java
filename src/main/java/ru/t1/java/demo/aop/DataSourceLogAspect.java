package ru.t1.java.demo.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;

import java.util.Arrays;

@Aspect
@RequiredArgsConstructor
@Component
public class DataSourceLogAspect {
    private DataSourceErrorLogRepository errorLogRepository;

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void daoLayer() {}

    @AfterThrowing(pointcut = "daoLayer()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        String errorMessage = ex.getMessage();
        String stackTrace = Arrays.toString(ex.getStackTrace());
        String methodSignature = joinPoint.getSignature().toShortString();

        DataSourceErrorLog errorLog = new DataSourceErrorLog(
                errorMessage,
                stackTrace,
                methodSignature
        );

        errorLogRepository.save(errorLog);
    }
}
