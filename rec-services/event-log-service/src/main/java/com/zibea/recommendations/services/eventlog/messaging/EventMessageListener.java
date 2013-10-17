package com.zibea.recommendations.services.eventlog.messaging;

import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.services.common.messages.event.EventMessageFactory;
import com.zibea.recommendations.services.common.messages.exception.EventMessageFactoryException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;

/**
 * @author Mikhail Bragin
 */
@Service
public class EventMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
       /* try {
            Event event = EventMessageFactory.parseEventMessage(message);
            System.out.println(event);
            Thread.sleep(5000l);
        } catch (EventMessageFactoryException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/
    }
}
