package ru.t1.java.demo.mapper;

import org.mapstruct.*;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountType;
import ru.t1.java.demo.model.dto.AccountDto;

import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    @Mapping(source = "type", target = "type", qualifiedByName = "accountTypeToString")
    @Mapping(source = "id", target = "id", qualifiedByName = "uuidToString")
    AccountDto toDto(Account account);

    @Mapping(source = "type", target = "type", qualifiedByName = "stringToAccountType")
    @Mapping(source = "id", target = "id", qualifiedByName = "stringToUuid")
    Account toEntity(AccountDto accountDto);

    @Named("accountTypeToString")
    static String accountTypeToString(AccountType type) {
        return type != null ? type.name() : null;
    }

    @Named("stringToAccountType")
    static AccountType stringToAccountType(String type) {
        return type != null ? AccountType.valueOf(type) : null;
    }

    @Named("uuidToString")
    static String uuidToString(UUID id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToUuid")
    static UUID stringToUuid(String id) {
        return id != null ? UUID.fromString(id) : null;
    }
}
