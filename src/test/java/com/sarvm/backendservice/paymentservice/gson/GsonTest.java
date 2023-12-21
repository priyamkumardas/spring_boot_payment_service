package com.sarvm.backendservice.paymentservice.gson;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import org.json.JSONObject;
//import org.junit.jupiter.api.Test;

public class GsonTest {

//    private static final String SUBSCRIPTION_ID = "subscriptionId";
//    private static final String RAZORPAY_ORDER_ID = "razorpay_order_id";
//    private static final String RAZORPAY_PAYMENT_ID = "razorpay_payment_id";
//    private static final String RAZORPAY_PAYMENT_STATUS = "razorpay_payment_status";
//
//    private static final String CODE = "code";
//    private static final String SERVICE = "service";
//    private static final String BODY = "body";
//
//    private static final String CODE_SUB_NOTIFICATION = "sub_notification";
//
//    @Test
//    public void test_gson_toJson_method(){
//        JSONObject body = new JSONObject();
//        Gson gson = new GsonBuilder().create();
//        body.put(SUBSCRIPTION_ID,"id_1");
//        body.put(RAZORPAY_ORDER_ID, "order_vagvdsy123");
//        body.put(RAZORPAY_PAYMENT_ID, "payment_vagvdsy123");
//        body.put(RAZORPAY_PAYMENT_STATUS, "paid");
//        String mssgBodyJsonString = gson.toJson(body);
//
//        JSONObject mssg = new JSONObject();
//        mssg.put(SERVICE,"pmt");
//        mssg.put(CODE, CODE_SUB_NOTIFICATION);
//        mssg.put(BODY, mssgBodyJsonString);
//        System.out.println(mssg);
//    }
//
//    @Test
//    public void test_pojo_fromJson_method(){
//        PaymentNotifyDto dto = new PaymentNotifyDto();
//        dto.setRazorpay_order_id("order_1");
//        dto.setRazorpay_payment_id("pay_1");
//        dto.setRazorpay_payment_status("paid");
//        dto.setSubscriptionId("id_1");
//        MessageBody<PaymentNotifyDto> body = new MessageBody<>(CODE_SUB_NOTIFICATION, "pmt", dto);
//        Gson gson = new GsonBuilder().create();
//        String serBody = gson.toJson(body);
//        System.out.println(serBody);
//        MessageBody<PaymentNotifyDto> desBody = gson.fromJson(serBody,MessageBody.class);
//        System.out.println("code => "+desBody.code);
//        System.out.println("service => "+desBody.service);
//        System.out.println("body => "+desBody.body);
//    }
//
//    private String getBody(){
//            JSONObject body = new JSONObject();
//            Gson gson = new GsonBuilder().create();
//            body.put(SUBSCRIPTION_ID,"id_1");
//            body.put(RAZORPAY_ORDER_ID, "order_vagvdsy123");
//            body.put(RAZORPAY_PAYMENT_ID, "payment_vagvdsy123");
//            body.put(RAZORPAY_PAYMENT_STATUS, "paid");
//            String mssgBodyJsonString = gson.toJson(body);
//            //System.out.println(mssgBodyJsonString);
//
//            JSONObject mssg = new JSONObject();
//            mssg.put(SERVICE,"pmt");
//            mssg.put(CODE, CODE_SUB_NOTIFICATION);
//            mssg.put(BODY, mssgBodyJsonString);
//            return mssg.toString();
//    }
}
