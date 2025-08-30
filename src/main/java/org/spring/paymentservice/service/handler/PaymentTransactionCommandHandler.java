package org.spring.paymentservice.service.handler;

public interface PaymentTransactionCommandHandler {
    void process(Long requestId, String message);
}
