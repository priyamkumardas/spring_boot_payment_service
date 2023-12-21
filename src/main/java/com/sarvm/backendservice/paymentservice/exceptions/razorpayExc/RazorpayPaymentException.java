package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class RazorpayPaymentException extends RazorpayException {
    public RazorpayPaymentException(String message) {
        super(message);
    }

    public RazorpayPaymentException(String message, Throwable cause) {
        super(message, cause);
    }

    public RazorpayPaymentException(Throwable cause) {
        super(cause);
    }

    public RazorpayPaymentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
