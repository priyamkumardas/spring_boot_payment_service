package com.sarvm.backendservice.paymentservice.service.razorpay.payment;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.response.RazorpayPaymentObjectAttr;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.sarvmUtils.RazorpayEntityMapper;
import com.sarvm.backendservice.paymentservice.service.razorpay.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RazorpayPaymentServiceTest extends BaseTest {

//    @Test
//    public void getPaymentByOrderId_should_return_List_of_payment_entity() throws RazorpayException {
//        RazorpayClient razorpayClient = getRazorpayClient();
//        String order_id = "order_K98TODLKlsjhcP";
//        List<Payment> payments = razorpayClient.orders.fetchPayments(order_id);
//        SarvmPgPayment sarvmPgPayment = new SarvmPgPayment();
//        sarvmPgPayment = RazorpayEntityMapper.fillPgPaymentValues(payments.get(0), sarvmPgPayment);
//        System.out.println(sarvmPgPayment);
//    }

//    @Test
//    public void payment_returned_should_be_convertible_to_SarvmPaymentEntity() throws RazorpayException {
//
//    }
}