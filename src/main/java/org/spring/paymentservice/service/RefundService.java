package org.spring.paymentservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.paymentservice.mapper.RefundMapper;
import org.spring.paymentservice.model.dto.CancelPaymentTransactionRequest;
import org.spring.paymentservice.model.dto.CancelPaymentTransactionResponse;
import org.spring.paymentservice.model.entity.Refund;
import org.spring.paymentservice.model.enums.RefundStatus;
import org.spring.paymentservice.repository.RefundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final PaymentTransactionService paymentTransactionService;
    private final RefundMapper refundMapper;

    public BigDecimal getTotalRefundedAmount(Long transactionId) {
        var refunds = refundRepository.findAllByPaymentTransactionId(transactionId);

        return refunds.stream()
                .map(Refund::getRefundedAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Transactional
    public CancelPaymentTransactionResponse cancelPayment(CancelPaymentTransactionRequest cancelPaymentRequest) {
        var sourceTransaction = paymentTransactionService.findOptionalById(cancelPaymentRequest.getTransactionId()).get();

        sourceTransaction.setAmount(
                sourceTransaction.getAmount().subtract(cancelPaymentRequest.getRefundedAmount())
        );

        var entity = refundRepository.save(
                refundMapper.toEntity(cancelPaymentRequest, RefundStatus.COMPLETED)
        );
        entity.setPaymentTransaction(sourceTransaction);

        return refundMapper.toResponse(entity);
    }
}
