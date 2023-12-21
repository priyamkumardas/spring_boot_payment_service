package com.sarvm.backendservice.paymentservice.commons.errMsg;

public class ErrorMsgMapping {

    public static final String ORDER_CREATION_ERROR = "AN ERROR " +
            "OCCURED WHILE CREATING AN ORDER AT PAYMENT_GATEWAY";

    public static final String PAYMENT_QUERY_ERROR  = "AN ERROR " +
            "OCCURED WHILE QUERYING A PAYMENT AT PAYMENT_GATEWAY";

    public static final String ORDER_NOT_CREATED = "Payment Gateway " +
            "Couldn't create order";
}
