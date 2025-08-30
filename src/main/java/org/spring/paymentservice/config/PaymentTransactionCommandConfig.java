package org.spring.paymentservice.config;

import org.spring.paymentservice.model.enums.PaymentTransactionCommand;
import org.spring.paymentservice.service.handler.CreatePaymentTransactionHandler;
import org.spring.paymentservice.service.handler.PaymentTransactionCommandHandler;
import org.spring.paymentservice.service.handler.RefundPaymentTransactionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class PaymentTransactionCommandConfig {

    @Bean
    public Map<PaymentTransactionCommand, PaymentTransactionCommandHandler> commandHandlers(
            CreatePaymentTransactionHandler createPaymentTransactionHandler,
            RefundPaymentTransactionHandler refundPaymentTransactionHandler
    ) {
        return Map.of(
        PaymentTransactionCommand.CREATE, createPaymentTransactionHandler,
        PaymentTransactionCommand.REFUND, refundPaymentTransactionHandler
        );
    }
}
