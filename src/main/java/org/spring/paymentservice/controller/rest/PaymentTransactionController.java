package org.spring.paymentservice.controller.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.TransactionsApi;
import org.openapitools.model.PaymentTransactionResponse;
import org.spring.paymentservice.service.PaymentTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentTransactionController implements TransactionsApi {
    private final PaymentTransactionService paymentTransactionService;

    @Override
    public ResponseEntity<PaymentTransactionResponse> transactionsTransactionIdGet(Long transactionId) {
        return ResponseEntity.ok(paymentTransactionService.findById(transactionId));
    }
}
