package com.sarvm.backendservice.paymentservice.exceptions.razorpayExc;

import com.razorpay.RazorpayException;

public class InvalidServiceNameException extends RazorpayException {

    public InvalidServiceNameException(String message) {
        super(message);
    }

    public InvalidServiceNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidServiceNameException(Throwable cause) {
        super(cause);
    }

    public InvalidServiceNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}