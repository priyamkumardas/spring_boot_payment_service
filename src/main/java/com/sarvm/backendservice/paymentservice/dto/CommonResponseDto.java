package com.sarvm.backendservice.paymentservice.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CommonResponseDto {

    public static Map<Object,Object> getGenericSuccessResponse(Object dto){
        Map<Object,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("data", dto);
        log.info("Success Response object prepared => "+dto);
        return map;
    }

    public static Map<Object,Object> getGenericFailureResponse(Object dto){
        Map<Object,Object> map = new HashMap<>();
        map.put("success",false);
        map.put("error", dto);
        log.info("Failure Response object prepared => "+dto);
        return map;
    }

}
