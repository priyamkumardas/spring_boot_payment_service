package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class RazorpayOrderException extends RazorpayException {
    public RazorpayOrderException(String message) {
        super(message);
    }

    public RazorpayOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public RazorpayOrderException(Throwable cause) {
        super(cause);
    }

    public RazorpayOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
