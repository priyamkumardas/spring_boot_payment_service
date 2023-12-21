package com.sarvm.backendservice.paymentservice.cronjobs.cronutils;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.response.RazorpayPaymentObjectAttr;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayOrderFetchException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentException;
import com.sarvm.backendservice.paymentservice.sarvmUtils.RzpPaymtStatus;
import com.sarvm.backendservice.paymentservice.service.ServiceEnum;
import com.sarvm.backendservice.paymentservice.service.razorpay.order.RazorpayOrderService;
import com.sarvm.backendservice.paymentservice.service.razorpay.payment.RazorpayPaymentService;
import com.sarvm.backendservice.paymentservice.service.sarvmService.SarvmAsyncDaoService;
import com.sarvm.backendservice.paymentservice.service.sarvmService.SarvmPaymentService;
import com.sarvm.backendservice.paymentservice.service.sarvmService.SarvmSyncDaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UpdatePaymentStatusUtils {

    private RazorpayPaymentService razorpayPaymentService;
    private RazorpayOrderService razorpayOrderService;
    private SarvmPaymentService sarvmPaymentService;
    private SarvmAsyncDaoService sarvmAsyncDaoService;
    private SarvmSyncDaoService sarvmSyncDaoService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePaymentStatusUtils.class);

    public UpdatePaymentStatusUtils(RazorpayPaymentService razorpayPaymentService,
                                    SarvmPaymentService sarvmPaymentService,
                                    SarvmAsyncDaoService sarvmAsyncDaoService,
                                    RazorpayOrderService razorpayOrderService,
                                    SarvmSyncDaoService sarvmSyncDaoService){
        this.razorpayPaymentService = razorpayPaymentService;
        this.sarvmPaymentService = sarvmPaymentService;
        this.sarvmAsyncDaoService = sarvmAsyncDaoService;
        this.razorpayOrderService = razorpayOrderService;
        this.sarvmSyncDaoService = sarvmSyncDaoService;
    }

    public void validatePgResponsePendingStatus(SarvmPgPayment sarvmPgPaymentEntity) throws RazorpayOrderFetchException {
        if(sarvmPgPaymentEntity == null) return;
        String razorpayOrderId = sarvmPgPaymentEntity.getPgOrderId();
        String orderMgtId = sarvmPgPaymentEntity.getSarvmPgOrder().getReceiptId();
        SarvmPgOrder order = sarvmPgPaymentEntity.getSarvmPgOrder();
        Optional<Order> orderOptional = razorpayOrderService.getOrderById(razorpayOrderId);
        if(orderOptional.isPresent()){
            sarvmSyncDaoService.updateSarvmPgOrder(orderOptional.get(),order,"razorpay");
        }
        try {
            Optional<Payment> payment = razorpayPaymentService.getPaymentById(sarvmPgPaymentEntity.getPgPaymentId());
            if(payment.isPresent()){
                if(payment.get().get("status").equals("failed")){
                    // scenario:- attempted the payment via UPI but the bank declined
                    // in this case i oticed that the same payment id which was in failed status was used again
                    // for the payment. so the same payment id was moved to authorised.
                    // so if the status at gateway is failed then it should not be touched and leave as it is
                    return;
                }
                sarvmSyncDaoService.savePaymentEntity(payment.get(),sarvmPgPaymentEntity);
                // notify the order service --> yet to be done
                if(sarvmPgPaymentEntity.getServiceName().equals(ServiceEnum.ordermgt.toString())){
                    sarvmAsyncDaoService.notifyOrderService(payment.get(), orderMgtId);
                }
                if(sarvmPgPaymentEntity.getServiceName().equals(ServiceEnum.subscription.toString())){
                    sarvmAsyncDaoService.notifySubscriptionService(payment.get(), orderMgtId);
                }
            }
        } catch (RazorpayPaymentException e) {
            LOGGER.error("CRON: -> Error Happened while fetch payment obj from razorpay");
        }
    }

    public void validatePgInitStatus(SarvmPgPayment p) throws RazorpayOrderFetchException {
        if(p == null) return;
        updateSarvmPgOrderHandler(p);
        String razorpayOrderId = p.getPgOrderId();
        String orderMgtId = p.getSarvmPgOrder().getReceiptId();
        Optional<List<Payment>> paymentListObj = razorpayPaymentService.getPaymentByOrderId(razorpayOrderId);
        if(paymentListObj.isPresent() && paymentListObj.get().size() == 1){
            Payment payment = paymentListObj.get().get(0);
            if(payment.get("status").equals("failed")){
                // scenario:- attempted the payment via UPI but the bank declined
                // in this case i oticed that the same payment id which was in failed status was used again
                // for the payment. so the same payment id was moved to authorised.
                // so if the status at gateway is failed then it should not be touched and leave as it is
                return;
            }
            // retreiving order mgmt service id to send it for notification
            LOGGER.info("CRON: -> Payment recieved from razorpay for single PG_INITSTATUS  => "+payment.toString());

            sarvmSyncDaoService.savePaymentEntity(payment, p);
            if(p.getServiceName().equals(ServiceEnum.ordermgt.toString())){
                sarvmAsyncDaoService.notifyOrderService(payment, orderMgtId);
            }
            if(p.getServiceName().equals(ServiceEnum.subscription.toString())){
                sarvmAsyncDaoService.notifySubscriptionService(payment, orderMgtId);
            }
        }else if(paymentListObj.isPresent() && paymentListObj.get().size() > 1){
            // delete the curret one and
            // create new one for each payment obj returned from razorpay
            SarvmPgOrder pgOrder = p.getSarvmPgOrder();
            sarvmPaymentService.deleteSarvmPgPayment(p);
            for(Payment payment: paymentListObj.get()){
                LOGGER.info("CRON: -> Payment recieved from razorpay for multiple PG_INITSTATUS => "+payment.toString());

                if(payment.get("status").equals("failed")){
                    continue;
                }
                if(payment.get("status").equals("authorised") || payment.get("status").equals("captured")){
                    this.sarvmPaymentService.deleteSarvmPgPayment(p);
                }
                Optional<SarvmPgPayment> presentSarvmPgEntity = this.sarvmPaymentService.ifSarvmPgEntityExists(payment.get("id"));
                if(presentSarvmPgEntity.isPresent()){
                    this.sarvmSyncDaoService.savePaymentEntity(payment,presentSarvmPgEntity.get());
                }else{
                    createNewSarvmPgEntity(pgOrder, payment);
                }
                if(p.getServiceName().equals(ServiceEnum.ordermgt.toString())){
                    sarvmAsyncDaoService.notifyOrderService(payment, orderMgtId);
                }
                if(p.getServiceName().equals(ServiceEnum.subscription.toString())){
                    sarvmAsyncDaoService.notifySubscriptionService(payment, orderMgtId);
                }
            }
        }
    }

    private void createNewSarvmPgEntity(SarvmPgOrder pgOrder, Payment payment) {
        SarvmPgPayment currPgPayment = new SarvmPgPayment();
        currPgPayment.setPgOrderId(pgOrder.getPgOrderId());
        currPgPayment.setSarvmPgOrder(pgOrder);
        currPgPayment.setPaymentGatewayName("RAZORPAY");
        currPgPayment.setServiceName(pgOrder.getServiceName());
        sarvmSyncDaoService.savePaymentEntity(payment, currPgPayment);
    }

    public void validatePgPendingStatus(SarvmPgPayment p) throws RazorpayOrderFetchException, RazorpayPaymentException {
        if(p==null) return;
        updateSarvmPgOrderHandler(p);
        Optional<Payment> payment = razorpayPaymentService.getPaymentById(p.getPgPaymentId());
        // if the payment is in status at payment gateway then we will capture it
        // by calling the pg api and update the status in our database
        if(payment.isPresent()){
            Payment objPgPaymt = payment.get();
            String pgId = objPgPaymt.get(RazorpayPaymentObjectAttr.ID);
            if (!objPgPaymt.get(RazorpayPaymentObjectAttr.STATUS).equals(RzpPaymtStatus.captured.toString().toLowerCase())
            && p.getPgStatus().equals(RzpPaymtStatus.authorized.toString().toLowerCase())) {
                // payment was captured at the pg gateway. will manually capture it.

                LOGGER.info("CRON: -> The current sarvmPgPpayment with id => { "+p.getId()+" } will be captured manually " +
                        "with pgId => "+pgId);
                SarvmPgOrder sarvmPgOrder = p.getSarvmPgOrder();
                double amount = sarvmPgOrder.getAmount()*100; // amount should be in paisa when sending to payment gateway
                String currency = sarvmPgOrder.getCurrency();
                Optional<Payment> capPgPayment = razorpayPaymentService.capturePayment(pgId, currency, amount);
                if(capPgPayment.isEmpty()){
                    LOGGER.info("CRON: -> Could not captured the sarvmPgPayment the status will remain in PG_PENDING status ");
                }else{
                    LOGGER.info("CRON: -> sarvmPgPayment was successfully captured the status will move to PG_CAPTURED status ");
                    // will be updating the database for sarmvPgPayment only as the sarvmPgOrder was already updated
                    // in the beginning
                    sarvmSyncDaoService.savePaymentEntity(capPgPayment.get(), p);
                }
            }else if(objPgPaymt.get(RazorpayPaymentObjectAttr.STATUS).equals(RzpPaymtStatus.captured.toString().toLowerCase())
                    && p.getPgStatus().equals(RzpPaymtStatus.authorized.toString().toLowerCase())){
                LOGGER.info("CRON: -> Payment is already captured at the gateway. Updatin the sarvmPgPayment id => "+
                        p.getId()+" pgId => "+pgId);
                // will be updating the database for sarmvPgPayment only as the sarvmPgOrder was already updated
                // in the beginning
                sarvmSyncDaoService.savePaymentEntity(objPgPaymt, p);
            }
        }
    }

    private void updateSarvmPgOrderHandler(SarvmPgPayment p) throws RazorpayOrderFetchException {
        String razorpayOrderId = p.getPgOrderId();
        // find the order details from the razorpay
        SarvmPgOrder order = p.getSarvmPgOrder();
        Optional<Order> orderOptional = razorpayOrderService.getOrderById(razorpayOrderId);
        if(orderOptional.isPresent()){
            sarvmSyncDaoService.updateSarvmPgOrder(orderOptional.get(),order,"razorpay");
        }
    }
}
