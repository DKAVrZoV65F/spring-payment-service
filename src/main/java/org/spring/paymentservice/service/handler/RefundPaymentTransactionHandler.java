package org.spring.paymentservice.service.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.paymentservice.controller.kafka.producer.PaymentTransactionProducer;
import org.spring.paymentservice.model.dto.CancelPaymentTransactionRequest;
import org.spring.paymentservice.model.enums.PaymentTransactionCommand;
import org.spring.paymentservice.service.RefundService;
import org.spring.paymentservice.service.validator.PaymentTransactionValidator;
import org.spring.paymentservice.util.JsonConverter;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundPaymentTransactionHandler implements PaymentTransactionCommandHandler {

    private final JsonConverter jsonConverter;
    private final PaymentTransactionValidator paymentTransactionValidator;
    private final RefundService refundService;
    private final PaymentTransactionProducer paymentTransactionProducer;

    @Override
    public void process(Long requestId, String message) {
        var request = jsonConverter.fromJson(message, CancelPaymentTransactionRequest.class);
        paymentTransactionValidator.validateCancelTransactionRequest(request);
        var result = refundService.cancelPayment(request);

        paymentTransactionProducer.sendCommandResult(requestId, PaymentTransactionCommand.REFUND, result.toString());
    }
}
