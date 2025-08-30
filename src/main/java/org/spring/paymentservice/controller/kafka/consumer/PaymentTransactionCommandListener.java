package org.spring.paymentservice.controller.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.spring.paymentservice.model.enums.PaymentTransactionCommand;
import org.spring.paymentservice.service.handler.PaymentTransactionCommandHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentTransactionCommandListener {

    private final Map<PaymentTransactionCommand, PaymentTransactionCommandHandler> commandHandlers;

    @KafkaListener(topics = "payment-command", containerFactory = "kafkaListenerContainerFactory")
    public void consumeCommand(ConsumerRecord<String, String> record) throws JsonProcessingException {
        log.info("Payment command received, command:{}", record);

        if (extractCommand(record).equals(PaymentTransactionCommand.UNKNOWN)) {
            throw new IllegalArgumentException("Unknown command");
        }

        commandHandlers.get(extractCommand(record)).processCommand(Long.valueOf(record.key()), record.value());
    }

    private PaymentTransactionCommand extractCommand(ConsumerRecord<String, String> record) {
        var commandHeader = record.headers().lastHeader("command");
        if (commandHeader != null) {
            return PaymentTransactionCommand.fromString(new String(commandHeader.value(), StandardCharsets.UTF_8));
        }
        return PaymentTransactionCommand.UNKNOWN;
    }
}
