package com.zibea.recommendations.webserver.core.business.impl;

import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.services.common.messages.event.EventMessageFactory;
import com.zibea.recommendations.services.common.messages.exception.EventMessageFactoryException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Sends processed event in message via messaging system
 *
 * @author Mikhail Bragin
 */
public class MessagingEventService extends EventService {

    @Autowired
    RabbitTemplate eventTemplate;

    @Override
    void processEvent(Event event) {
        Message message = null;
        try {
            message = EventMessageFactory.buildEventMessage(event);
        } catch (EventMessageFactoryException e) {
            log.error("Error while preparing event message " + event, e);
        }
        eventTemplate.send(message);
    }
}
