package com.sarvm.backendservice.paymentservice.controller.razorpay.api;

import com.razorpay.Order;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.request.OrderCreateRequestAttr;
import com.sarvm.backendservice.paymentservice.dto.CommonResponseDto;
import com.sarvm.backendservice.paymentservice.dto.request.order.OrderCreateRequestDto;
import com.sarvm.backendservice.paymentservice.dto.response.order.OrderResponseDto;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.InvalidServiceNameException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayOrderException;
import com.sarvm.backendservice.paymentservice.service.razorpay.order.RazorpayOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/pmt/apis/v1/razorpay/order")
@Slf4j
public class OrderController {

    @Autowired
    RazorpayOrderService razorpayOrderService;

    @PostMapping("/")
    public ResponseEntity<Object> createPgOrder(@RequestBody OrderCreateRequestDto requestDto)
            throws RazorpayOrderException, InvalidServiceNameException {
        log.info("Payment OrderId request recieved for the phone-number : => "+requestDto.
                getNotes().get(OrderCreateRequestAttr.PHONE) + " user id: => "+
                requestDto.getNotes().get(OrderCreateRequestAttr.PHONE));
        Optional<Order> orderDetails = razorpayOrderService.createOrder(requestDto);
        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setOrder_id(orderDetails.get().get("id"));
        responseDto.setPayment_gateway_name("RAZORPAY");
        log.info("OrderResponse being sent for the phone-number : => "+requestDto.
                getNotes().get(OrderCreateRequestAttr.PHONE)+
                requestDto.getNotes().get(OrderCreateRequestAttr.PHONE));
        return ResponseEntity.status(HttpStatus.CREATED).body(CommonResponseDto
                .getGenericSuccessResponse(responseDto));
    }


}
