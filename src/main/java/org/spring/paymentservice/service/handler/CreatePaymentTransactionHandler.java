package org.spring.paymentservice.service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.paymentservice.controller.kafka.producer.PaymentTransactionProducer;
import org.spring.paymentservice.mapper.PaymentTransactionMapper;
import org.spring.paymentservice.model.dto.CreatePaymentTransactionRequest;
import org.spring.paymentservice.model.entity.BankAccount;
import org.spring.paymentservice.model.enums.PaymentTransactionCommand;
import org.spring.paymentservice.model.enums.PaymentTransactionStatus;
import org.spring.paymentservice.service.BankAccountService;
import org.spring.paymentservice.service.PaymentTransactionService;
import org.spring.paymentservice.service.validator.PaymentTransactionValidator;
import org.spring.paymentservice.util.JsonConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePaymentTransactionHandler implements PaymentTransactionCommandHandler {

    private final JsonConverter jsonConverter;
    private final PaymentTransactionValidator paymentTransactionValidator;
    private final BankAccountService bankAccountService;
    private final PaymentTransactionMapper paymentTransactionMapper;
    private final PaymentTransactionService paymentTransactionService;
    private final PaymentTransactionProducer paymentTransactionProducer;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void process(Long requestId, String message) {
        var request = jsonConverter.fromJson(message, CreatePaymentTransactionRequest.class);
        paymentTransactionValidator.validateCreateTransactionRequest(request);

        var sourceAccount = bankAccountService.findOptionalById(request.getSourceAccountId()).get();
        BankAccount destinationBankAccount = null;
        if (request.getDestinationAccountId() != null) {
            destinationBankAccount = bankAccountService.findOptionalById(request.getDestinationAccountId()).get();
        }

        var result = paymentTransactionService.save(
                paymentTransactionMapper.toEntity(request, sourceAccount, destinationBankAccount, PaymentTransactionStatus.SUCCESS)
        );
        paymentTransactionProducer.sendCommandResult(requestId, PaymentTransactionCommand.CREATE, result.toString());
    }
}
