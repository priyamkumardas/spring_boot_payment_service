package com.sarvm.backendservice.paymentservice.sarvmUtils;

import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;

public enum RzpPaymtStatus {
    created,
    authorized,
    captured,
    refunded,
    failed
}
