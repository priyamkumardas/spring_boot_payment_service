package com.sarvm.backendservice.paymentservice.service.sarvmService;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.response.RazorpayPaymentObjectAttr;
import com.sarvm.backendservice.paymentservice.dto.request.payment.PaymentStatusCheckRequestDto;
import com.sarvm.backendservice.paymentservice.dto.response.payment.PaymentStatusCheckResponseDto;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmUserPaymentMap;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.InvalidPaymentSignatureException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayOrderFetchException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentFetchException;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmOrderRepository;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmPaymentRepository;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmUserPaymentMapRepository;
import com.sarvm.backendservice.paymentservice.sarvmUtils.RzpPaymtStatus;
import com.sarvm.backendservice.paymentservice.service.razorpay.order.RazorpayOrderService;
import com.sarvm.backendservice.paymentservice.service.razorpay.payment.RazorpayPaymentService;
import com.sarvm.backendservice.paymentservice.sarvmUtils.HmacUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SarvmPaymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SarvmPaymentService.class);
    private final RazorpayPaymentService razorpayPaymentService;
    private final RazorpayOrderService razorpayOrderService;
    private final SarvmAsyncDaoService sarvmAsyncDaoService;
    private final ISarvmOrderRepository sarvmOrderRepository;
    private final ISarvmPaymentRepository sarvmPaymentRepository;
    private final ISarvmUserPaymentMapRepository iSarvmUserPaymentMapRepository;

    @Value("${razorpayApiPassword}")
    private String apiSecret;

    @Autowired
    public SarvmPaymentService(RazorpayPaymentService razorpayPaymentService,
                               SarvmAsyncDaoService sarvmAsyncDaoService,
                               ISarvmOrderRepository sarvmOrderRepository,
                               ISarvmPaymentRepository sarvmPaymentRepository,
                               RazorpayOrderService razorpayOrderService, ISarvmUserPaymentMapRepository iSarvmUserPaymentMapRepository) {
        this.razorpayPaymentService = razorpayPaymentService;
        this.sarvmAsyncDaoService = sarvmAsyncDaoService;
        this.sarvmOrderRepository = sarvmOrderRepository;
        this.sarvmPaymentRepository = sarvmPaymentRepository;
        this.razorpayOrderService = razorpayOrderService;
        this.iSarvmUserPaymentMapRepository = iSarvmUserPaymentMapRepository;

    }

    public Optional<PaymentStatusCheckResponseDto> checkStatus(PaymentStatusCheckRequestDto requestDto)
            throws InvalidPaymentSignatureException, RazorpayPaymentFetchException {
        LOGGER.info("status check request recieved ==> " + requestDto);
        boolean valid = validatePaymentSignature(requestDto);

        if (!valid) {
            LOGGER.error("Could not validate the payment singature for phone-number: " + requestDto.getPhone_number() +
                    " user-id: " + requestDto.getUser_id());
            throw new InvalidPaymentSignatureException("payment signature invalid");
        }
        SarvmPgPayment sarvmPgPayment = retrieveSarvmPaymentEntity(requestDto);
        Optional<Payment> payment;
        try {
            payment = razorpayPaymentService.getPaymentById(requestDto.getRazorpay_payment_id());
        } catch (RazorpayPaymentException e) {
            sarvmAsyncDaoService.handleNoPaymentFetchScenario(sarvmPgPayment, requestDto);
            LOGGER.error("service failed to fetch payment information from razorpay phone-number: " + requestDto.getPhone_number() +
                    " user-id: " + requestDto.getUser_id());
            throw new RazorpayPaymentFetchException("unable to recieve response from payment gateway");
        }
        if (payment.isPresent()) {
            LOGGER.info("Saving payment recieved from status check request --> " + requestDto.getRazorpay_payment_id() +
                    " phone-number : " + requestDto.getPhone_number() +
                    " user-id: " + requestDto.getUser_id());
            Payment authorisedPayment = payment.get();
            Payment capturedPayment = handlePaymentStatusWise(authorisedPayment, sarvmPgPayment);
            if (capturedPayment != null) {
                LOGGER.info("Payment was successfully captured manually => " + capturedPayment);
                authorisedPayment = capturedPayment;
            }
            System.out.println("authorisedPayment="+authorisedPayment);
            System.out.println("sarvmPgPayment="+sarvmPgPayment);
            sarvmAsyncDaoService.savePaymentEntity(authorisedPayment, sarvmPgPayment);
            return prepareStatusCheckResponse(Optional.of(authorisedPayment));
        }
        return Optional.empty();
    }

    private Payment handlePaymentStatusWise(Payment payment, SarvmPgPayment sarvmPgPayment) {
        if (payment.get(RazorpayPaymentObjectAttr.STATUS).equals(RzpPaymtStatus.authorized.toString().toLowerCase())) {
            // capture the payment manually because we don't know certainly whether the payment
            // will be captured by the pg gateway or not.
            LOGGER.info("The payment is in authorised state only. Will be manually capturing " +
                    "it. PgId => " + payment.get(RazorpayPaymentObjectAttr.ID));
            double amount = sarvmPgPayment.getSarvmPgOrder().getAmount() * 100;
            String paymentId = payment.get(RazorpayPaymentObjectAttr.ID);
            String currency = sarvmPgPayment.getSarvmPgOrder().getCurrency();
            Optional<Payment> capturedPg = razorpayPaymentService.capturePayment(paymentId, currency, amount);
            if (capturedPg.isPresent()) {
                LOGGER.info("The payment is successfully captured. Will update the database. PgId => " +
                        "" + payment.get(RazorpayPaymentObjectAttr.ID));
                return capturedPg.get();
            }
            LOGGER.info("The payment could not be captured. Will set the sarvmPgPayment status to PG_PENDING . PgId => " +
                    "" + payment.get(RazorpayPaymentObjectAttr.ID));
            return capturedPg.get();
        } else if (payment.get(RazorpayPaymentObjectAttr.STATUS).equals(RzpPaymtStatus.failed.toString().toLowerCase())) {
            // because now the payment is failed it might be possible that the payment will be reattempted
            // by the client. We will mark the current sarvmPgEntity as failed and will create a new
            // sarvmPgentity with a pgInit status and save it to the database
            LOGGER.info("The payment got failed at Gateway. Will update the database to failed status. PgId => " +
                    "" + payment.get(RazorpayPaymentObjectAttr.ID));
            SarvmPgOrder sarvmPgOrder = sarvmPgPayment.getSarvmPgOrder();
            sarvmAsyncDaoService.initAndSaveSarvmPgPayment(sarvmPgOrder);
            return null;
        }
        LOGGER.info("The payment is already captured. No manual captured is required.");
        return null;
    }

    private SarvmPgPayment retrieveSarvmPaymentEntity(PaymentStatusCheckRequestDto requestDto) {

        SarvmPgOrder sarvmPgOrder = sarvmOrderRepository.findByPgOrderId(requestDto.getRazorpay_order_id());
        try {
            Optional<Order> orderOptional = razorpayOrderService.getOrderById(requestDto.getRazorpay_order_id());
            orderOptional.ifPresent(order -> sarvmAsyncDaoService.updateSarvmPgOrder(order, sarvmPgOrder, "razorpay"));
        } catch (RazorpayOrderFetchException exc) {
            LOGGER.info("Couldn't fetch the order for updating -> " + sarvmPgOrder.getPgOrderId());
        }

        List<SarvmPgPayment> sarvmPgPaymentList = getPgPaymentByOrderIdAndPgStatus(
                sarvmPgOrder.getPgOrderId(), PaymentStatus.PG_INIT.toString());

        if (sarvmPgPaymentList.size() == 0) {
            sarvmPgPaymentList = getPgPaymentByOrderIdAndPgStatus(
                    sarvmPgOrder.getPgOrderId(), PaymentStatus.PG_RESPONSE_PENDING.toString());
            if (sarvmPgPaymentList.size() != 0) return sarvmPgPaymentList.get(0);
            return null;
        }
        return sarvmPgPaymentList.get(0);
    }

    private List<SarvmPgPayment> getPgPaymentByOrderIdAndPgStatus(String orderId, String paymentStatus) {
        return sarvmPaymentRepository.findAllByPgOrderIdAndPaymentStatus(
                orderId, paymentStatus);
    }

    private boolean validatePaymentSignature(PaymentStatusCheckRequestDto requestDto) {
        String data = requestDto.getRazorpay_order_id() + "|" + requestDto.getRazorpay_payment_id();
        String secret = this.apiSecret;
        String genSignature = HmacUtil.hmacWithApacheCommons("HmacSha256", data, secret);
        return genSignature.equals(requestDto.getRazorpay_payment_signature());
    }

    private Optional<PaymentStatusCheckResponseDto> prepareStatusCheckResponse(Optional<Payment> payment) {
        PaymentStatusCheckResponseDto responseDto = new PaymentStatusCheckResponseDto();
        if (payment.isPresent()) {
            responseDto.setStatus(payment.get().get(RazorpayPaymentObjectAttr.STATUS));
            responseDto.setPayment_id(payment.get().get(RazorpayPaymentObjectAttr.ID));
            responseDto.setOrder_id(payment.get().get(RazorpayPaymentObjectAttr.ORDER_ID));
        }
        return Optional.of(responseDto);

    }

    public SarvmPgPayment saveSarvmPgPayment(SarvmPgPayment paymentEntity) {
        if (paymentEntity.getId() != null) {
            LOGGER.info("Updating " + SarvmPgPayment.class + " object --> " + paymentEntity);
        } else {
            LOGGER.info("Creating " + SarvmPgPayment.class + " object --> " + paymentEntity);
        }

        return this.sarvmPaymentRepository.save(paymentEntity);
    }

    public void deleteSarvmPgPayment(SarvmPgPayment paymentEntity) {
        LOGGER.info("Deleting " + SarvmPgPayment.class + " object --> " + paymentEntity);
        this.sarvmPaymentRepository.delete(paymentEntity);
    }

    public SarvmPgOrder saveSarvmPgOrder(SarvmPgOrder sarvmPgOrder) {
        if (sarvmPgOrder.getId() == null) {
            LOGGER.info("Creating order entity to the database --> " + sarvmPgOrder);
        } else {
            LOGGER.info("Updating order entity to the database --> " + sarvmPgOrder);
        }
        sarvmPgOrder = this.sarvmOrderRepository.save(sarvmPgOrder);

        return sarvmPgOrder;
    }

    public Optional<SarvmPgOrder> findOrderByRazorpayOrderId(String orderId) {
        SarvmPgOrder sarvmPgOrder = this.sarvmOrderRepository.findByPgOrderId(orderId);
        return Optional.of(sarvmPgOrder);
    }

    public SarvmPgPayment findSingleSarvmPgPaymentByOrderId(String orderId, String status) {
        List<SarvmPgPayment> sarvmPgPayments;
        if (status != null) {
            sarvmPgPayments = this.sarvmPaymentRepository.findAllByPgOrderIdAndPaymentStatus(orderId, status);
        } else {
            sarvmPgPayments = this.sarvmPaymentRepository.findAllByPgOrderId(orderId);
        }
        if (sarvmPgPayments.size() == 0) {
            return null;
        }
        return sarvmPgPayments.get(0);
    }

    public Optional<SarvmPgPayment> ifSarvmPgEntityExists(String pgId) {
        return this.sarvmPaymentRepository.findFirstByPgPaymentId(pgId);
    }

    public List<SarvmPgPayment> getPaymentStatus(String userId) {

        List<SarvmPgPayment> sarvmPgPayments = sarvmPaymentRepository.findByUserId(userId);
        return sarvmPgPayments;
    }

    public List<SarvmPgPayment> getAllData() {
        List<SarvmPgPayment> list = this.sarvmPaymentRepository.findAll();
        return list;
    }

    public List<SarvmPgPayment> getPaymentStatusRecordsBetweenDates(Date timestamp20MinBefore, Date currentTimestamp) {
        return sarvmPaymentRepository.findAllByCreatedAtBetweenAndPaymentStatus(timestamp20MinBefore, currentTimestamp,"PG_CAPTURED");
    }
}
