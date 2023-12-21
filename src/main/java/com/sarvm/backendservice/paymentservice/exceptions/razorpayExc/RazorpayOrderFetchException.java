package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class RazorpayOrderFetchException extends RazorpayException {

    public RazorpayOrderFetchException(String message) {
        super(message);
    }

    public RazorpayOrderFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public RazorpayOrderFetchException(Throwable cause) {
        super(cause);
    }

    public RazorpayOrderFetchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
