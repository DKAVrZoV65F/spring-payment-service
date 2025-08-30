package org.spring.paymentservice.controller.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.paymentservice.model.enums.PaymentTransactionCommand;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentTransactionProducer {

    private static final String PAYMENT_TRANSACTION_COMMAND_TYPE_HEADER = "commandType";
    private static final String TOPIC = "payment-command-result";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public <T> void sendCommandResult(Long requestId, PaymentTransactionCommand commandType, String message) {
        var kafkaMessage = buildMessage(commandType, requestId, message);

        kafkaTemplate.send(kafkaMessage);
        log.info("Sent command result: {}", kafkaMessage);
    }

    private Message<String> buildMessage(PaymentTransactionCommand commandType, Long requestId, String payload) {
        return MessageBuilder
                .withPayload(payload)
                .setHeader(KafkaHeaders.TOPIC, TOPIC)
                .setHeader(KafkaHeaders.KEY, requestId)
                .setHeader(PAYMENT_TRANSACTION_COMMAND_TYPE_HEADER, commandType)
                .build();
    }
}
