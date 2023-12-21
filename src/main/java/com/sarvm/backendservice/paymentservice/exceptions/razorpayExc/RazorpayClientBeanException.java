package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class RazorpayClientBeanException extends RazorpayException {

    public RazorpayClientBeanException(String message) {
        super(message);
    }

    public RazorpayClientBeanException(String message, Throwable cause) {
        super(message, cause);
    }

    public RazorpayClientBeanException(Throwable cause) {
        super(cause);
    }

    public RazorpayClientBeanException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
