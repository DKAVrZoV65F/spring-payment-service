package org.spring.paymentservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.paymentservice.mapper.BankAccountMapper;
import org.spring.paymentservice.model.entity.BankAccount;
import org.spring.paymentservice.model.exception.BankAccountValidationException;
import org.spring.paymentservice.repository.BankAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Transactional
    public BankAccountResponse findById(@NotNull Long id) {
        var entity = bankAccountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Bank account with id " + id + " not found")
        );
        return bankAccountMapper.toDto(entity);
    }

    @Transactional
    public Optional<BankAccount> findOptionalById(@NotNull Long id) {
        try {
            return bankAccountRepository.findById(id);
        } catch (EntityNotFoundException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<BankAccount> findByAccount(String accountNumber) {
        return Optional.ofNullable(bankAccountRepository.findByNumber(accountNumber));
    }


    @Transactional
    public BankAccountResponse save(BankAccountCreateRequest request) {
        if (findByAccount(request.getNumber()).isPresent()) {
            throw new BankAccountValidationException(List.of("Account number " + request.getNumber() + " already exists."));
        }
        var entity = bankAccountRepository.save(
                bankAccountMapper.toEntity(request)
        );
        return bankAccountMapper.toDto(entity);
    }
}
