//package com.sarvm.backendservice.paymentservice.messaging.queue.publish;
//
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
//import org.springframework.context.annotation.Primary;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//
//@Service
////@Primary
//public class SqsQueuePublisher implements IQueuePublisher{
//
//    @Autowired
//    private QueueMessagingTemplate queueMessagingTemplate;
//
//    public void sendMessage(String queueEndpoint, Object mssg){
//        queueMessagingTemplate.send(queueEndpoint, MessageBuilder.withPayload(mssg).build());
//    }
//}
