package com.sarvm.backendservice.paymentservice.exceptions;

import com.sarvm.backendservice.paymentservice.dto.CommonResponseDto;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {RazorpayOrderException.class})
    public ResponseEntity<Object> handleRazorpayOrderException(RazorpayOrderException exception){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                    exception.getMessage(),
                    badRequest,
                    ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }

    @ExceptionHandler(value = {RazorpayOrderFetchException.class})
    public ResponseEntity<Object> handleRazorpayOrderException(RazorpayOrderFetchException exception){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }

    @ExceptionHandler(value = {RazorpayPaymentException.class})
    public ResponseEntity<Object> handleRazorpayOrderException(RazorpayPaymentException exception){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }

    @ExceptionHandler(value = {RazorpayClientBeanException.class})
    public ResponseEntity<Object> handleRazorpayClientBeanException(RazorpayClientBeanException exception){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }

    @ExceptionHandler(value = {InvalidPaymentSignatureException.class})
    public ResponseEntity<Object> handleInvalidPaymentSignatureException(InvalidPaymentSignatureException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }

    @ExceptionHandler(value = {RazorpayPaymentFetchException.class})
    public ResponseEntity<Object> handleRazorpayPaymentFetchException(RazorpayPaymentFetchException exception){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }

    @ExceptionHandler(value = {InvalidServiceNameException.class})
    public ResponseEntity<Object> handleInvalidServiceNameException(InvalidServiceNameException exception){
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorReponsePayload payload = new ErrorReponsePayload(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(CommonResponseDto.getGenericFailureResponse(payload),badRequest);
    }
}
