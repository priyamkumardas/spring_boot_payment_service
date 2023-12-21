package com.sarvm.backendservice.paymentservice.messaging.queue.publish;

public interface IQueuePublisher {
    public void sendMessage(String queueEndpoint, Object mssg);
}
