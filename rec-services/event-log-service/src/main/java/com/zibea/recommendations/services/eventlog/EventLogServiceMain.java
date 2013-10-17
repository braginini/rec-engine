package com.zibea.recommendations.services.eventlog;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mikhail Bragin
 */
public class EventLogServiceMain {

    public static void main(String[] arg) throws Exception {

        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("eventlog-service-context.xml");
            /*RabbitTemplate template = (RabbitTemplate) context.getBean("eventTemplate");
            //template.convertAndSend("event.queue", new Message("d".getBytes(), new MessageProperties()));

            List<Long> ids = new ArrayList<>();
            ids.add(1l);
            ids.add(2l);
            Event<List<Long>> event = new Event<>(Event.EventType.PURCHASE, "1", "2", 1l, null, ids);
            Message message = EventMessageFactory.buildEventMessage(event);
            template.send("event.queue", message);*/
        } catch (Throwable e) {
            //log.error( "something happend while starting server", e );
            e.printStackTrace();
            System.exit(1);
        }

    }
}
