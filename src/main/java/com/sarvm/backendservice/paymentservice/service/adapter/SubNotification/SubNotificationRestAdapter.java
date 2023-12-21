package com.sarvm.backendservice.paymentservice.service.adapter.SubNotification;

import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.sarvmUtils.WebServiceUtils;
import com.sarvm.backendservice.paymentservice.service.message.ISubscriptionNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@Primary
public class SubNotificationRestAdapter implements ISubscriptionNotification {

    @Value("${sub_endpoint1}")
    private String subEndpoint;

    @Value("${load_balancer_url}")
    private String load_balancer_url;

    @Value("${subscriptionServiceToken}")
    private String tokenValue;

    public void sendPostRequest(Payment payment, String id) {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("subscriptionId",id);
        jsonData.put("razorpay_order_id",payment.get("order_id"));
        jsonData.put("razorpay_payment_id",payment.get("id"));
        jsonData.put("razorpay_payment_status",payment.get("status"));
        String token = "Bearer "+tokenValue;
        WebServiceUtils webServiceUtils = WebServiceUtils.getWebServiceUtils();
        String subscriptionServiceUrl = load_balancer_url+subEndpoint;
        webServiceUtils.postRequest(subscriptionServiceUrl,jsonData,token);
        log.info("Notified client subscription system for payment id -> "+payment.get("id")+" to url => "+subscriptionServiceUrl);
    }
}
