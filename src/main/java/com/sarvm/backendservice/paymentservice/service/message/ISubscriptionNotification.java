package com.sarvm.backendservice.paymentservice.service.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.razorpay.Payment;

public interface ISubscriptionNotification {
    public void sendPostRequest(Payment payment, String id);
}
