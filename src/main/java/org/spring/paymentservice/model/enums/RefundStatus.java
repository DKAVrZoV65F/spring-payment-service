package org.spring.paymentservice.model.enums;

import lombok.Getter;

@Getter
public enum RefundStatus {
    COMPLETED,
    FAILED;




    public static RefundStatus fromString(String status) {
        for (RefundStatus refundStatus : RefundStatus.values()) {
            if (refundStatus.name().equalsIgnoreCase(status)) {
                return refundStatus;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown RefundStatus value: %s.", status));
    }
}
