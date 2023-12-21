package com.sarvm.backendservice.paymentservice.gson;

public class MessageBody <T>{

    String code;
    String service;
    T body;

    public MessageBody(String c, String s, T b){
        body = b;
        code = c;
        service = s;
    }
}
