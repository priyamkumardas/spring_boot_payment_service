package com.sarvm.backendservice.paymentservice.service.razorpay;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

public abstract class BaseTest {

    protected RazorpayClient razorpayClient;

    protected RazorpayClient getRazorpayClient() throws RazorpayException {
        if(razorpayClient == null){
            return new RazorpayClient("rzp_test_Q5GonNjspPNdtm",
                    "GJZukxiviKBkWI1Dx3EphzE3");
        }
        return razorpayClient;
    }
}
