package org.spring.paymentservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.spring.paymentservice.model.dto.CancelPaymentTransactionRequest;
import org.spring.paymentservice.model.dto.CancelPaymentTransactionResponse;
import org.spring.paymentservice.model.dto.enums.CommandResultStatus;
import org.spring.paymentservice.model.entity.Refund;
import org.spring.paymentservice.model.enums.RefundStatus;

@Mapper(componentModel = "spring")
public interface RefundMapper {
    Refund toEntity(CancelPaymentTransactionRequest request, RefundStatus status);


    @Mapping(source = "status", target = "status", qualifiedByName = "mapRefundStatusToCommandStatus")
    CancelPaymentTransactionResponse toResponse(Refund refund);

    @Named("mapRefundStatusToCommandStatus")
    default CommandResultStatus mapRefundStatusToCommandStatus(RefundStatus refundStatus) {
        if (refundStatus == null) {
            return CommandResultStatus.FAILED;
        }
        return switch (refundStatus) {
            case COMPLETED -> CommandResultStatus.SUCCESS;
            case FAILED -> CommandResultStatus.FAILED;
        };
    }
}
