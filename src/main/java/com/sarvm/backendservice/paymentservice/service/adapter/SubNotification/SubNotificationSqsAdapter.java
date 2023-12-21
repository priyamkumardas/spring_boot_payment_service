//package com.sarvm.backendservice.paymentservice.service.adapter.SubNotification;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.razorpay.Payment;
//import com.sarvm.backendservice.paymentservice.messaging.queue.publish.IQueuePublisher;
//import com.sarvm.backendservice.paymentservice.service.message.ISubscriptionNotification;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Primary;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
////@Service
////@Primary
//@Slf4j
//public class SubNotificationSqsAdapter implements ISubscriptionNotification {
//
//    private static final String RAZORPAY_ORDER_ID = "order_id";
//    private static final String RAZORPAY_PAYMENT_ID = "id";
//    private static final String RAZORPAY_PAYMENT_STATUS = "status";
//
//    private static final String CODE_SUB_NOTIFICATION = "sub_notification";
//
//    @Value("${aws.sqs.queue.sub.uri}")
//    private String queueEndpoint;
//
//    private IQueuePublisher queuePublisher;
//
//    public SubNotificationSqsAdapter(IQueuePublisher queuePublisher) {
//        this.queuePublisher = queuePublisher;
//    }
//
//    public void sendPostRequest(Payment payment, String id){
//        PaymentNotifyDto dto = new PaymentNotifyDto();
//        dto.setRazorpay_order_id(payment.get(RAZORPAY_ORDER_ID));
//        dto.setRazorpay_payment_id(payment.get(RAZORPAY_PAYMENT_ID));
//        dto.setRazorpay_payment_status(payment.get(RAZORPAY_PAYMENT_STATUS));
//        dto.setSubscriptionId(id);
//        MessageBody<PaymentNotifyDto> body = new MessageBody<>(CODE_SUB_NOTIFICATION, "pmt", dto);
//        ObjectMapper Obj = new ObjectMapper();
//        String jsonStr = null;
//        try {
//            jsonStr = Obj.writeValueAsString(body);
//            log.info("jsonStr => "+jsonStr);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        queuePublisher.sendMessage(queueEndpoint, jsonStr);
//        log.info("Notified Subscription service for payment id -> "+payment.get("id")+" via Queue => "+queueEndpoint);
//    }
//}
