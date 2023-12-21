package com.sarvm.backendservice.paymentservice.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping("/pmt/apis/v1")
public class DockerTestController {

    @Value("${buildNumber}")
    private String build;

    @GetMapping("/healthcheck")
    public ResponseEntity<Object> dockerHealthStatusCheck(){

        Map<Object,Object> map = new HashMap<>();
        map.put("success",true);
        Map<Object,Object> map2 = new HashMap<>();
        map2.put("ts", LocalDateTime.now());
        map.put("buildNumber", build);
        map.put("serviceName", "pmt");
        map.put("data", map2);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

}
