package com.sarvm.backendservice.paymentservice.sarvmUtils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class WebServiceUtils {


    RestTemplate restTemplate;

    private static WebServiceUtils webServiceUtils = new WebServiceUtils();

    public static WebServiceUtils getWebServiceUtils(){
        return webServiceUtils;
    }

    private WebServiceUtils(){
        restTemplate = new RestTemplate();
    }

    public ResponseEntity<String> postRequest(String url, Object requestBody, String token){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.POST, request, String.class);

        System.out.println("Contact Res ===> "+response.getBody());
        return response;
    }


}
