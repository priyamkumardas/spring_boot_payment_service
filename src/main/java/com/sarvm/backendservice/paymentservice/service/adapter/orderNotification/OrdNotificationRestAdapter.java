package com.sarvm.backendservice.paymentservice.service.adapter.orderNotification;

import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.sarvmUtils.WebServiceUtils;
import com.sarvm.backendservice.paymentservice.service.message.IOrderNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Primary
@Slf4j
public class OrdNotificationRestAdapter implements IOrderNotification {

    @Value("${order_endpoint1}")
    private String order_endpoint;

    @Value("${orderServiceToken}")
    private String token;


    @Value("${load_balancer_url}")
    private String load_balancer_url;

    public void sendPostRequest(Payment payment, String id) {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("order_id",id);
        jsonData.put("razorpay_order_id",payment.get("order_id"));
        jsonData.put("razorpay_payment_id",payment.get("id"));
        jsonData.put("razorpay_payment_status",payment.get("status"));
        WebServiceUtils webServiceUtils = WebServiceUtils.getWebServiceUtils();
        String orderServiceUrl = load_balancer_url+order_endpoint;
        webServiceUtils.postRequest(orderServiceUrl,jsonData,"Bearer "+token);
        log.info("Notified client order system for payment id -> "+payment.get("id")+" to url => "+orderServiceUrl);
    }
}
