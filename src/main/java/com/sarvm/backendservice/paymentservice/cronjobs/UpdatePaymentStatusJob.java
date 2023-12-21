package com.sarvm.backendservice.paymentservice.cronjobs;

import com.sarvm.backendservice.paymentservice.cronjobs.cronutils.UpdatePaymentStatusUtils;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.Enums.PaymentStatus;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmUserPaymentMap;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayOrderFetchException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentException;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmOrderRepository;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmPaymentRepository;
import com.sarvm.backendservice.paymentservice.repository.paymentDetails.ISarvmUserPaymentMapRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class UpdatePaymentStatusJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatePaymentStatusJob.class);

    UpdatePaymentStatusUtils updatePaymentStatusUtils;
    ISarvmPaymentRepository sarvmPaymentRepository;
    private final ISarvmOrderRepository sarvmOrderRepository;
    private final ISarvmUserPaymentMapRepository iSarvmUserPaymentMapRepository;

    @Value("${cron.fixed_delay}")
    private String fixedDelayProps;

    @Value("${cron.initial_delay}")
    private String initialDelayProps;

    private final String fixedDelay;
    private final String initalDelay;

    @Autowired
    public UpdatePaymentStatusJob(ISarvmPaymentRepository sarvmPaymentRepository,
                                  UpdatePaymentStatusUtils updatePaymentStatusUtils, ISarvmOrderRepository sarvmOrderRepository, ISarvmUserPaymentMapRepository iSarvmUserPaymentMapRepository) {
        this.sarvmPaymentRepository = sarvmPaymentRepository;
        this.updatePaymentStatusUtils = updatePaymentStatusUtils;
        this.sarvmOrderRepository = sarvmOrderRepository;
        this.iSarvmUserPaymentMapRepository = iSarvmUserPaymentMapRepository;
        this.fixedDelay = fixedDelayProps;
        this.initalDelay = initialDelayProps;
    }


    @Scheduled(fixedDelay = 1200000, initialDelay = 600000)
    public void updatePaymentInitOrResPendingStatus() {

//         this job will run every 10 minutes
//         it has to sync the status from razorpay payment gateway to our sarvm database
//         this method handles two statuses -> PG_INIT, PG_RESPONSE_PENDING
//         for all these payment entities the actual status has to be updated from the razorpay
//
//         for pg_init status we don't have payment_id, so we have to use order_id stored for that
//         payment entity and use that to query razorpay website. If the payment was done then
//         we will sync up or else we will leave it as it is.
//
//         for pg_response_pending we have the payment id stored in the database so we can directly
//         query that and update the status as present in the razorpay.
//
//         pagination needs to present so that the query can be optimised. And they haved to be sorted
//         based upon there integer id. Because that will ive us the oldest record present and that needs
//         to be updated on priority. (Check --> Pagination and Sorting)

//        if the payment is in authorised state then we will capture it by calling payment gateway api

        // define page size and page number for pagination
        int pagesize = 20;
        int pageNo = 0;

        for (pageNo = 0; ; pageNo++) {

            Pageable pageable = PageRequest.of(pageNo, pagesize);
            Page<SarvmPgPayment> paymentEntityPage = sarvmPaymentRepository
                    .findAllByPaymentStatusOrPaymentStatusOrPaymentStatus(PaymentStatus.PG_INIT.toString(),
                            PaymentStatus.PG_RESPONSE_PENDING.toString(), PaymentStatus.PG_PENDING.toString(), pageable);

            if (paymentEntityPage.isEmpty()) break;

            List<SarvmPgPayment> paymentEntityList = paymentEntityPage.getContent();

            paymentEntityList.forEach(p -> {
                String status = p.getPaymentStatus();
                if (status.equals(PaymentStatus.PG_INIT.toString())) {
                    try {
                        updatePaymentStatusUtils.validatePgInitStatus(p);
                    } catch (RazorpayOrderFetchException e) {
                        e.printStackTrace();
                    }
                } else if (status.equals(PaymentStatus.PG_RESPONSE_PENDING.toString())) {
                    try {
                        updatePaymentStatusUtils.validatePgResponsePendingStatus(p);
                    } catch (RazorpayOrderFetchException e) {
                        e.printStackTrace();
                    }
                } else if (status.equals(PaymentStatus.PG_PENDING.toString())) {
                    try {
                        updatePaymentStatusUtils.validatePgPendingStatus(p);
                    } catch (RazorpayOrderFetchException | RazorpayPaymentException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }


    //  <---------------------- Scheduler Reference ---------------->

//    @Scheduled(fixedRate = 5000)
//    public void fixedRateSch() throws InterruptedException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//
//        Date now = new Date();
//        String strDate = sdf.format(now);
//        System.out.println("Fixed Rate scheduler:: " + strDate);
//        Thread .sleep(2000);
//    }

//    @Scheduled(fixedDelay = 3000, initialDelay = 1000)
//    public void fixedDelaySch() throws InterruptedException {
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        Thread.sleep(2000); // overall it should execute every 5 second
//        Date now = new Date();
//        String strDate = sdf.format(now);
//        System.out.println("Fixed Rate scheduler:: " + strDate);
//    }


    @Scheduled(fixedDelay = 600000, initialDelay = 5000)
    public void savePaymentMappingDataIntoThirdTable() throws InterruptedException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        //That query used insert record not present in third table(Using outer join)
        List<SarvmPgOrder> lastAddedRecords = sarvmOrderRepository.findAllWhichNotPresentInThirdTable();

        LOGGER.info("List Size :  " + lastAddedRecords.size());
        for (SarvmPgOrder paymentEntity : lastAddedRecords) {
            SarvmUserPaymentMap userPaymentMap = iSarvmUserPaymentMapRepository.findByPgOrderId(paymentEntity.getPgOrderId());
            if (userPaymentMap == null) {
                SarvmUserPaymentMap sarvmUserPaymentMap = new SarvmUserPaymentMap();
                sarvmUserPaymentMap.setPgOrderId(paymentEntity.getPgOrderId());
                JSONObject jsonObj = new JSONObject(paymentEntity.getNotes());
                String userID = (String) jsonObj.get("userId");
                sarvmUserPaymentMap.setUserId(userID);
                sarvmUserPaymentMap.setCreationTime(paymentEntity.getCreationTime());

                LOGGER.info("creating " + SarvmUserPaymentMap.class + " object --> " + sarvmUserPaymentMap);
                LOGGER.info("Record Available  : " +sarvmUserPaymentMap.toString());
               iSarvmUserPaymentMapRepository.save(sarvmUserPaymentMap);

            }
        }
    }
}

