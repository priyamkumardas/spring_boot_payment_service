package com.sarvm.backendservice.paymentservice.service.adapter.SubNotification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody<T>{

    private String code;
    private String service;
    private T body;
}
