package com.sarvm.backendservice.paymentservice.service.adapter.SubNotification;

import lombok.Data;

@Data
public class PaymentNotifyDto {

    private String subscriptionId;

    private String razorpay_order_id;

    private String razorpay_payment_id;

    private  String razorpay_payment_status;
}
