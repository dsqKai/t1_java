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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="data_source_error_logs")
public class DataSourceErrorLog extends AbstractPersistable<Long> {
    @Column
    String message;

    @Column(name="exception_description")
    String exceptionDescription;

    @Column(name = "method_signature")
    String methodSignature;
}
