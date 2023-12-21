package com.sarvm.backendservice.paymentservice.gson;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PaymentNotifyDto {

    private String subscriptionId;

    private String razorpay_order_id;

    private String razorpay_payment_id;

    private  String razorpay_payment_status;
}
