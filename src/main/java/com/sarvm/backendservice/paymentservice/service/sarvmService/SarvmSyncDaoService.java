package com.sarvm.backendservice.paymentservice.service.sarvmService;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmOrderRepository;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmPaymentRepository;
import com.sarvm.backendservice.paymentservice.sarvmUtils.RazorpayEntityMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SarvmSyncDaoService {

    ISarvmPaymentRepository sarvmPaymentRepository;
    ISarvmOrderRepository sarvmOrderRepository;

    public SarvmSyncDaoService(ISarvmPaymentRepository paymentRepository,
                               ISarvmOrderRepository sarvmOrderRepository) {
        this.sarvmPaymentRepository = paymentRepository;
        this.sarvmOrderRepository = sarvmOrderRepository;
    }


    public void savePaymentEntity(Payment payment, SarvmPgPayment sarvmPgPayment){
        sarvmPgPayment = RazorpayEntityMapper.fillPgPaymentValues(payment,sarvmPgPayment);
        sarvmPaymentRepository.save(sarvmPgPayment);
        log.info("saved sarvmPaymentEntity while server to server status check  ----> "+sarvmPgPayment);
    }

    public void updateSarvmPgOrder(Order order, SarvmPgOrder sarvmPgOrder, String pg_name){
        sarvmPgOrder = RazorpayEntityMapper.razorpayToOrderEntity(order, sarvmPgOrder, pg_name);

        sarvmOrderRepository.save(sarvmPgOrder);
    }
}
