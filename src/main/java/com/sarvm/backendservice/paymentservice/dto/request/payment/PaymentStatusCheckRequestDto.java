package com.sarvm.backendservice.paymentservice.dto.request.payment;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
public class PaymentStatusCheckRequestDto {

    @NonNull
    private String razorpay_order_id;

    @NonNull
    private String razorpay_payment_id;

    @NonNull
    private String razorpay_payment_signature;

    private String phone_number;

    private String user_id;

}
