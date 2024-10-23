package ru.t1.java.demo.mapper;

import org.mapstruct.*;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    @Mapping(source = "accountId", target = "account.id")
    Transaction toEntity(TransactionDto transactionDto);

    @Mapping(source = "account.id", target = "accountId")
    TransactionDto toDto(Transaction transaction);
}