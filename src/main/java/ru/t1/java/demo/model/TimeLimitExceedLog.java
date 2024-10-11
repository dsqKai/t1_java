package ru.t1.java.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="time_limit_exceed_logs")
public class TimeLimitExceedLog extends AbstractPersistable<Long> {
    @Column(name = "method_signature")
    String methodSignature;

    @Column(name = "execution_time")
    Long executionTime;
}
