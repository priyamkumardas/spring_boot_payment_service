package com.sarvm.backendservice.paymentservice.service.razorpay.order;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.request.OrderCreateRequestAttr;
import com.sarvm.backendservice.paymentservice.commons.errMsg.ErrorMsgMapping;
import com.sarvm.backendservice.paymentservice.dto.request.order.OrderCreateRequestDto;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.InvalidServiceNameException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayOrderException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayOrderFetchException;
import com.sarvm.backendservice.paymentservice.service.ServiceEnum;
import com.sarvm.backendservice.paymentservice.service.sarvmService.SarvmAsyncDaoService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RazorpayOrderService {

    private static Logger LOGGER = LoggerFactory.getLogger(RazorpayOrderService.class);

    @Autowired
    RazorpayClient razorpayClient;

    @Autowired
    SarvmAsyncDaoService sarvmAsyncDaoService;

    private static final List<String> services = List.of(ServiceEnum.ordermgt.toString(), ServiceEnum.subscription.toString());

    // Create an order
    public Optional<Order> createOrder(OrderCreateRequestDto requestDto) throws RazorpayOrderException, InvalidServiceNameException {

        // Fill the mandatory value of the order request as requested
        // from razorpay payment gateway
        if(!services.contains(requestDto.getService())){
            throw new InvalidServiceNameException("service name is not correct ");
        }

        JSONObject orderRequest = new JSONObject();
        //Amount pay needs to be in decimal(paisa) for reserve pay
        double amount = Math.round(requestDto.getAmount() * 100.0) / 100.0;
        orderRequest.put(OrderCreateRequestAttr.AMOUNT, amount*100);
        orderRequest.put(OrderCreateRequestAttr.CURRENCY,requestDto.getCurrency());
        orderRequest.put(OrderCreateRequestAttr.RECEIPT, requestDto.getReceipt_id());
        JSONObject payment = new JSONObject();
        payment.put(OrderCreateRequestAttr.CAPTURE,"automatic");
        JSONObject captureOptions = new JSONObject();
        captureOptions.put(OrderCreateRequestAttr.AUTOMATIC_EXPIRY_PERIOD,12);
        captureOptions.put(OrderCreateRequestAttr.MANUAL_EXPIRY_PERIOD,7200);
        captureOptions.put(OrderCreateRequestAttr.REFUND_SPEED,"optimum");
        payment.put(OrderCreateRequestAttr.CAPTURE_OPTIONS,captureOptions);
        //orderRequest.put(OrderCreateRequestAttr.PAYMENT,payment);
        JSONObject notes = new JSONObject();

        if(requestDto.getNotes() !=null){
            for(String key:requestDto.getNotes().keySet()){
                notes.put(key, requestDto.getNotes().get(key));
            }
            orderRequest.put(OrderCreateRequestAttr.NOTES,notes);
        }

        if(requestDto.isPartial_payment()){
            orderRequest.put(OrderCreateRequestAttr.RECEIPT,true);
        }

        if(requestDto.getFirst_payment_min_amount() != null){
            orderRequest.put(OrderCreateRequestAttr.FIRST_PAY_MIN_AMOUNT,
                    requestDto.getFirst_payment_min_amount().intValue());
        }

        try {
            Order order = razorpayClient.orders.create(orderRequest);
            LOGGER.info("Razorpay Order created => "+order.toString());
            sarvmAsyncDaoService.createAndSaveSarvmPgOrder(order,"RAZORPAY", requestDto);
            return Optional.of(order);
        } catch (RazorpayException e) {
            LOGGER.error(ErrorMsgMapping.ORDER_CREATION_ERROR);
            throw new RazorpayOrderException(ErrorMsgMapping.ORDER_NOT_CREATED);
        }

    }

    // Fetch orders



    // Fetch order by order id
    public Optional<Order> getOrderById(String orderId) throws RazorpayOrderFetchException {
        try {
            Order order = razorpayClient.orders.fetch(orderId);
            return Optional.of(order);
        } catch (RazorpayException e) {
            throw new RazorpayOrderFetchException("Couldn't load order from payment gateway");
        }
    }


    // Fetch payments for an order


    // Update order
}
