package com.sarvm.backendservice.paymentservice.service.razorpay.webhooks;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.OrderStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.sarvmUtils.RazorpayEntityMapper;
import com.sarvm.backendservice.paymentservice.service.sarvmService.SarvmPaymentService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class RazorpayWebhooksService {

    private final Logger LOGGER = LoggerFactory.getLogger(RazorpayWebhooksService.class);

    @Autowired
    private SarvmPaymentService sarvmPaymentService;

    private static final String ID = "id";
    private static final String ORDER_ID = "order_id";
    private static final String STATUS = "status";

    public SarvmPgOrder extractSarvmPgOrder(Map<Object,Object> requestBody){
        Order order = new Order(retriveEntity( requestBody, "order"));
        LOGGER.info("recieved order payload from razorpay webhook -> " + order);
        String status = order.get("status");
        String orderId = order.get(ID);
        Optional<SarvmPgOrder> sarvmPgOrder = this.sarvmPaymentService.findOrderByRazorpayOrderId(order.get(ID));

        // making this change because we are not persisting any order_id which is being generated for subscription
        // service. So that will not be present in sarvmPgOrder enitity. Hence add null safety via Optial wrapping
        if(sarvmPgOrder.isPresent()){
            sarvmPgOrder.get().setOrderStatus(OrderStatus.PAID.toString());
        }
        return sarvmPaymentService.saveSarvmPgOrder(sarvmPgOrder.get());
    }

    public SarvmPgPayment extractSarvmPgPayment(Map<Object,Object> requestBody){
        Payment payment = new Payment(retriveEntity(requestBody, "payment"));
        LOGGER.info("recieved payment payload from razorpay webhook -> " + payment);
        SarvmPgPayment sarvmPgPayment = sarvmPaymentService.findSingleSarvmPgPaymentByOrderId(
                payment.get(ORDER_ID), null);
        sarvmPgPayment = RazorpayEntityMapper.fillPgPaymentValues(payment,sarvmPgPayment);
        return sarvmPaymentService.saveSarvmPgPayment(sarvmPgPayment);
    }

    private static JSONObject retriveEntity(Map<Object,Object> requestBody, String entity){
        JSONObject jsonObj = new JSONObject(requestBody);
        JSONObject payload = (JSONObject)jsonObj.get("payload");
        JSONObject orderJson   = (JSONObject)((JSONObject)payload.get(entity)).get("entity");
        return orderJson;
    }

}
