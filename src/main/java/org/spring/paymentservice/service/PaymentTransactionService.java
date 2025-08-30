package org.spring.paymentservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.paymentservice.mapper.PaymentTransactionMapper;
import org.spring.paymentservice.model.dto.CreatePaymentTransactionResponse;
import org.spring.paymentservice.model.entity.PaymentTransaction;
import org.spring.paymentservice.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.openapitools.model.PaymentTransactionResponse;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentTransactionMapper paymentTransactionMapper;

    @Transactional
    public CreatePaymentTransactionResponse save(PaymentTransaction paymentTransaction) {
        var entity = paymentTransactionRepository.save(paymentTransaction);
        return paymentTransactionMapper.toKafkaDto(entity);
    }

    @Transactional
    public Optional<PaymentTransaction> findOptionalById(@NotNull Long id){
        try{
            return paymentTransactionRepository.findById(id);
        } catch (EntityNotFoundException e){
            return Optional.empty();
        }
    }

    @Transactional
    public PaymentTransactionResponse findById(@NotNull Long id){
        var entity = paymentTransactionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Payment transaction with id " + id + " not found")
        );
        return paymentTransactionMapper.toDto(entity);
    }
}
