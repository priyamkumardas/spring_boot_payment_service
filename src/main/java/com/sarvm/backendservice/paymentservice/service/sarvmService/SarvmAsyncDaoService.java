package com.sarvm.backendservice.paymentservice.service.sarvmService;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.request.WebhooksEventConstants;
import com.sarvm.backendservice.paymentservice.dto.request.order.OrderCreateRequestDto;
import com.sarvm.backendservice.paymentservice.dto.request.payment.PaymentStatusCheckRequestDto;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmOrderRepository;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmPaymentRepository;
import com.sarvm.backendservice.paymentservice.sarvmUtils.RazorpayEntityMapper;
import com.sarvm.backendservice.paymentservice.service.message.IOrderNotification;
import com.sarvm.backendservice.paymentservice.service.message.ISubscriptionNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SarvmAsyncDaoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SarvmAsyncDaoService.class);

    private ISarvmOrderRepository sarvmOrderRepository;
    private ISarvmPaymentRepository sarvmPaymentRepository;
    private IOrderNotification clientOrderMgmtService;
    private ISubscriptionNotification clientSubscriptionService;

    @Autowired
    public SarvmAsyncDaoService(ISarvmOrderRepository sarvmOrderRepository,
                                ISarvmPaymentRepository sarvmPaymentRepository,
                                IOrderNotification clientOrderMgmtService,
                                ISubscriptionNotification clientSubscriptionService) {
        this.sarvmOrderRepository = sarvmOrderRepository;
        this.sarvmPaymentRepository = sarvmPaymentRepository;
        this.clientOrderMgmtService = clientOrderMgmtService;
        this.clientSubscriptionService = clientSubscriptionService;
    }

    @Async
    public void createAndSaveSarvmPgOrder(Order order, String pg_name, OrderCreateRequestDto requestDto){
        SarvmPgOrder sarvmPgOrder = new SarvmPgOrder();
        sarvmPgOrder.setServiceName(requestDto.getService());
        sarvmPgOrder = RazorpayEntityMapper.razorpayToOrderEntity(order,sarvmPgOrder,pg_name);
        sarvmPgOrder = sarvmOrderRepository.save(sarvmPgOrder);
        LOGGER.info("Creating sarvmPgOrder ==> "+sarvmPgOrder);
        initAndSaveSarvmPgPayment(sarvmPgOrder);
    }

    @Async
    public void updateSarvmPgOrder(Order order, SarvmPgOrder sarvmPgOrder, String pg_name){
        sarvmPgOrder = RazorpayEntityMapper.razorpayToOrderEntity(order, sarvmPgOrder, pg_name);
        sarvmPgOrder = sarvmOrderRepository.save(sarvmPgOrder);
    }

    @Async
    public void savePaymentEntity(Payment payment, SarvmPgPayment sarvmPgPayment){
        sarvmPgPayment = RazorpayEntityMapper.fillPgPaymentValues(payment,sarvmPgPayment);
        sarvmPgPayment = sarvmPaymentRepository.save(sarvmPgPayment);
        LOGGER.info("saved sarvmPaymentEntity while server to server status check  ----> "+sarvmPgPayment);
    }

    public void initAndSaveSarvmPgPayment(SarvmPgOrder sarvmPgOrder){
        // CAUTION:- This method must only be called during the creation of sarvm order entity
        // Otherwise it will create huge non fixable issues
        SarvmPgPayment sarvmPgPayment = new SarvmPgPayment();
        sarvmPgPayment.setPaymentStatus(PaymentStatus.PG_INIT.toString());
        sarvmPgPayment.setPaymentGatewayName(sarvmPgOrder.getPaymentGatewayName());
        sarvmPgPayment.setSarvmPgOrder(sarvmPgOrder);
        sarvmPgPayment.setPgOrderId(sarvmPgOrder.getPgOrderId());
        sarvmPgPayment.setServiceName(sarvmPgOrder.getServiceName());
        sarvmPaymentRepository.save(sarvmPgPayment);
        LOGGER.info("init paymentEntity ----> "+sarvmPgPayment);
    }

    @Async
    public void handleNoPaymentFetchScenario(SarvmPgPayment sarvmPgPayment, PaymentStatusCheckRequestDto requestDto){
        if(sarvmPgPayment == null) return;
        sarvmPgPayment.setPaymentStatus(PaymentStatus.PG_RESPONSE_PENDING.toString());
        sarvmPgPayment.setPgPaymentId(requestDto.getRazorpay_payment_id());
        sarvmPaymentRepository.save(sarvmPgPayment);
        LOGGER.info("saving sarvmPaymentEntity while pg_responsing_pending ----> "+sarvmPgPayment);
    }

    @Async
    public void notifyOrderService(Payment payment, String orderMgtId) {
        if(payment.get(WebhooksEventConstants.STATUS).equals(WebhooksEventConstants.AUTHORIZED)
                || payment.get(WebhooksEventConstants.STATUS).equals(WebhooksEventConstants.CAPTURED)){
            clientOrderMgmtService.sendPostRequest(payment, orderMgtId);
        }
    }

    @Async
    public void notifySubscriptionService(Payment payment, String subscriptionId) {
        if(payment.get(WebhooksEventConstants.STATUS).equals(WebhooksEventConstants.AUTHORIZED)
                || payment.get(WebhooksEventConstants.STATUS).equals(WebhooksEventConstants.CAPTURED)){
            clientSubscriptionService.sendPostRequest(payment, subscriptionId);
        }
    }
}
