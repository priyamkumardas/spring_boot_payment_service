package com.sarvm.backendservice.paymentservice.service.razorpay.payment;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RazorpayPaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RazorpayPaymentService.class);

    @Autowired
    RazorpayClient razorpayClient;

    // find payment by id
    public Optional<Payment> getPaymentById(String paymentId) throws RazorpayPaymentException {
        try {
            Payment payment = razorpayClient.payments.fetch(paymentId);
            return Optional.of(payment);
        } catch (RazorpayException e) {
            throw new RazorpayPaymentException("Error happened while fetching payment for id: "+ paymentId);
        }
    }

    // retrieve payments linked to an order
    public Optional<List<Payment>> getPaymentByOrderId(String orderId){
        try {
            List<Payment> payment = razorpayClient.orders.fetchPayments(orderId);
            return Optional.of(payment);
        } catch (RazorpayException e) {
            LOGGER.error("Error happened while fetching payment for order id => "+orderId);
            return Optional.empty();
        }
    }

    // capture a payment
    public Optional<Payment> capturePayment(String paymentId, String currency, double amount){

        JSONObject paymentRequest = new JSONObject();
        paymentRequest.put("amount", (int)amount);
        paymentRequest.put("currency", currency);

        try {
            Payment payment = razorpayClient.payments.capture(paymentId, paymentRequest);
            return Optional.of(payment);
        } catch (RazorpayException e) {
            return Optional.empty();
        }
    }

    // find all payments

    // retrieve card or expanded details

    // retrieve card details of a payment

    // edit an existing payment
}
