package com.sarvm.backendservice.paymentservice.configuration;

import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.sarvm.backendservice.paymentservice.exceptions.razorpayExc.RazorpayClientBeanException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
public class RazorpayConfigurations {

    @Value("${razorpayApiKey}")
    private String razorpayApiKey;

    @Value("${razorpayApiPassword}")
    private String razorpayApiPassword;

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Bean
    public RazorpayClient razorpayClient() throws RazorpayClientBeanException {
        RazorpayClient razorpayClient = null;
        try{
            System.out.println();
            razorpayClient = new RazorpayClient(razorpayApiKey, razorpayApiPassword );
            return razorpayClient;
        }catch(RazorpayException e){
            log.error("Razorpay exception happened while creating razorpay client");
            throw new RazorpayClientBeanException("Couldn't not create razorpay client");
        }
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
