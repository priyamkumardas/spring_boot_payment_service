package com.sarvm.backendservice.paymentservice.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorReponsePayload {

    private String message;
    private HttpStatus status;
    private ZonedDateTime timeStamp;
}
