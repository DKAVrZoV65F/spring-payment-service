package org.spring.paymentservice.model.dto;

import lombok.Data;
import org.spring.paymentservice.model.dto.enums.CommandResultStatus;

@Data
public class CancelPaymentTransactionResponse {

    private CommandResultStatus status;

    private String errorMessage;
}
