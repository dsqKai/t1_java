package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.demo.model.TimeLimitExceedLog;
import ru.t1.java.demo.model.dto.TimeLimitLogDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MetricMapper {
    TimeLimitLogDto toDto(TimeLimitExceedLog log);

    TimeLimitExceedLog toEntity(TimeLimitLogDto dto);
}
