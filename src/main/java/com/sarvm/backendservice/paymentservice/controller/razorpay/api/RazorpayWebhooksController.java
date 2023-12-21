package com.sarvm.backendservice.paymentservice.controller.razorpay.api;

import com.sarvm.backendservice.paymentservice.commons.constants.razorpay.request.WebhooksEventConstants;
import com.sarvm.backendservice.paymentservice.service.razorpay.webhooks.RazorpayWebhooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/pmt/apis/v1/razorpay/webhooks")
public class RazorpayWebhooksController {

    @Autowired
    private RazorpayWebhooksService razorpayWebhooksService;

    @PostMapping("/")
    public void saveWebhookPayload(@RequestBody Map<Object,Object> eventPayload){
        if(eventPayload.get("event").equals(WebhooksEventConstants.ORDER_PAID_EVENT)){
            razorpayWebhooksService.extractSarvmPgOrder(eventPayload);
        }
        if(eventPayload.get("event").equals(WebhooksEventConstants.PAYMENT_CAPTURE_EVENT)){
            razorpayWebhooksService.extractSarvmPgPayment(eventPayload);
        }
    }
}
