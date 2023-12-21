package com.sarvm.backendservice.paymentservice.controller.razorpay.api;

import com.sarvm.backendservice.paymentservice.dto.CommonResponseDto;
import com.sarvm.backendservice.paymentservice.dto.request.order.OrderCreateRequestDto;
import com.sarvm.backendservice.paymentservice.dto.request.payment.PaymentStatusCheckRequestDto;
import com.sarvm.backendservice.paymentservice.dto.response.payment.PaymentStatusCheckResponseDto;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgOrder;
import com.sarvm.backendservice.paymentservice.entity.sarvmPaymentDetails.SarvmPgPayment;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.InvalidPaymentSignatureException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayPaymentFetchException;
import com.sarvm.backendservice.paymentservice.service.sarvmService.SarvmPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pmt/apis/v1/razorpay/payment")
@Slf4j
public class PaymentController {

    @Autowired
    SarvmPaymentService sarvmPaymentService;

    @PostMapping("/status")
    public ResponseEntity<Object> getPaymentStatusCheck
            (@RequestBody PaymentStatusCheckRequestDto requestDto) throws RazorpayPaymentException,
            InvalidPaymentSignatureException, RazorpayPaymentFetchException {
        log.info("Payment status check request recieved for the phone-number: => " + requestDto.getPhone_number() + requestDto);
        Optional<PaymentStatusCheckResponseDto> responseDto = sarvmPaymentService.checkStatus(requestDto);
        if (responseDto.isPresent()) {
            log.info("successful response generated for payment status check: => " + responseDto.get() + " phone-number: "
                    + requestDto.getPhone_number() +
                    " user-id: " + requestDto.getUser_id());
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(CommonResponseDto
                    .getGenericSuccessResponse(responseDto.get()));
        }
        log.info("failure response generated for payment status check: =>  phone-number: "
                + requestDto.getPhone_number() +
                " user-id: " + requestDto.getUser_id());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonResponseDto
                .getGenericFailureResponse(responseDto.get()));
    }

    //Payment INFO API
    @GetMapping("/getPaymentStatus/{userId}")
    public ResponseEntity<Object> getcallSarvmPaymentService(@PathVariable("userId") String userId) {
        List<SarvmPgPayment> list = null;
        list = sarvmPaymentService.getPaymentStatus(userId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(CommonResponseDto
                .getGenericSuccessResponse(list));
    }


    //fetch getPaymentStatusRecordsBetweenDates records
    @GetMapping("/lastTwentyMinutesRecords/{start}/{end}")
    public ResponseEntity<Object> getPaymentStatusRecordsBetweenDates(@PathVariable("start") Long startDate, @PathVariable("end") Long endDate
    ) {
        List<SarvmPgPayment> list = null;

        Date start = new Date(startDate);
        Date end = new Date(endDate);
        list = sarvmPaymentService.getPaymentStatusRecordsBetweenDates(start, end);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(CommonResponseDto
                .getGenericSuccessResponse(list));
    }

    @GetMapping("/getAllDataPayment")
    public List<SarvmPgPayment> getAllDataPaymentService() {
        List<SarvmPgPayment> list = null;
        list = sarvmPaymentService.getAllData();
        return list;
    }
}
