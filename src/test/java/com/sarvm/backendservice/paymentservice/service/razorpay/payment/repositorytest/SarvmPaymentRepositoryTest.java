package com.sarvm.backendservice.paymentservice.service.razorpay.payment.repositorytest;

import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmPaymentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

//@SpringBootTest
public class SarvmPaymentRepositoryTest {

//    @Autowired
//    ISarvmPaymentRepository sarvmPaymentRepository;
//
////    @Test
////    public void test_query_by_payment_status_of_pgInit_and_PgResponsePending(){
////        List<SarvmPgPayment> paymentEntities = sarvmPaymentRepository.findAllByPaymentStatusOrPaymentStatus(PaymentStatus.PG_INIT.toString(), PaymentStatus.PG_RESPONSE_PENDING.toString());
////        Assertions.assertEquals(112, paymentEntities.size() );
////    }
}
