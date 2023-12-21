package com.sarvm.backendservice.paymentservice.service.message;

import com.razorpay.Payment;

public interface IOrderNotification {

    public void sendPostRequest(Payment payment, String id);
}
