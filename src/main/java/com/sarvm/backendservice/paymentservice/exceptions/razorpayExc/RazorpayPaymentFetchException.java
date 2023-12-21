package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class RazorpayPaymentFetchException extends RazorpayException {
    public RazorpayPaymentFetchException(String message) {
        super(message);
    }

    public RazorpayPaymentFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public RazorpayPaymentFetchException(Throwable cause) {
        super(cause);
    }

    public RazorpayPaymentFetchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

