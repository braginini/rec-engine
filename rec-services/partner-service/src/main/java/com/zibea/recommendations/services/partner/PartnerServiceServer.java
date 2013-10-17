package com.zibea.recommendations.services.partner;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Mikhail Bragin
 */
public class PartnerServiceServer {

    @Autowired
    RabbitAdmin rabbitAdmin;

    private final String serviceQueue;

    public PartnerServiceServer(String serviceQueue) {
        this.serviceQueue = serviceQueue;

    }

    @PostConstruct
    public void init() {
        rabbitAdmin.declareQueue(new Queue(serviceQueue));
        rabbitAdmin.purgeQueue(serviceQueue, true);
    }

    public void start() {

    }

    @PreDestroy
    public void destroy() {

    }
}
