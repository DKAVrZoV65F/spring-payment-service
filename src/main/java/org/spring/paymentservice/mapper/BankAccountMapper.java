package org.spring.paymentservice.mapper;

import org.mapstruct.Mapper;
import org.spring.paymentservice.model.entity.BankAccount;
import org.openapitools.model.BankAccountCreateRequest;
import org.openapitools.model.BankAccountResponse;

@Mapper(componentModel = "spring")
public interface BankAccountMapper {

    BankAccountResponse toDto(BankAccount bankAccount);
    BankAccount toEntity(BankAccountCreateRequest bankAccountResponse);
}
