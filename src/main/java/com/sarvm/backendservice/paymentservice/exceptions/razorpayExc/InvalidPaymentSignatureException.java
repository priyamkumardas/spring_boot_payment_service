package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class InvalidPaymentSignatureException extends RazorpayException {
    public InvalidPaymentSignatureException(String message) {
        super(message);
    }

    public InvalidPaymentSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPaymentSignatureException(Throwable cause) {
        super(cause);
    }

    public InvalidPaymentSignatureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
