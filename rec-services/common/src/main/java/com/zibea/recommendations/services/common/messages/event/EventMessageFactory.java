package com.zibea.recommendations.services.common.messages.event;

import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.common.model.event.ItemViewEvent;
import com.zibea.recommendations.common.model.event.PurchaseEvent;
import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.EventMessageFactoryException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.utils.SerializationUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author Mikhail Bragin
 */
public class EventMessageFactory {

    private static final String ENCODING = Charset.defaultCharset().name();
    public static final String EVENT_MSG_HEADER = "eventType";


    public static Message buildEventMessage(Event event) throws EventMessageFactoryException {

        ObjectMapper mapper = new ObjectMapper();

        MessageProperties messageProperties = new MessageProperties();

        messageProperties.setType(String.valueOf(MessageType.EVENT.getType()));

        messageProperties.setHeader(EVENT_MSG_HEADER, String.valueOf(event.getType().getType()));

        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);

        try {
            Message message = new Message(mapper.writeValueAsBytes(event), messageProperties);
            return message;
        } catch (IOException e) {
            throw new EventMessageFactoryException("Error while building event message", e);
        }

    }

    @NotNull
    public static Event parseEventMessage(Message message) throws EventMessageFactoryException {

        /*MessageProperties messageProperties = message.getMessageProperties();

        MessageType messageType = MessageType.lookup(Integer.valueOf(messageProperties.getType()));

        if (messageType != MessageType.EVENT)
            throw new EventMessageFactoryException("Wrong message type " + message);

        Event.EventType eventType = extractEventType(messageProperties);

        if (eventType == null)
            throw new EventMessageFactoryException("Wrong event type " + message);

        ObjectMapper mapper = new ObjectMapper();

        try {

            switch (eventType) {
                case ITEM_VIEW:
                    return mapper.readValue(getMessageBodyAsString(message), ItemViewEvent.class);
                case PURCHASE:
                    return mapper.readValue(getMessageBodyAsString(message), PurchaseEvent.class);
                default:
                    break;

            }
        } catch (IOException e) {
            throw new EventMessageFactoryException("Error while parsing event message", e);
        }

        throw new EventMessageFactoryException("Event type " + eventType + " was not found");*/
        return null;
    }

    private static String getMessageBodyAsString(Message message) {
        byte[] body = message.getBody();
        MessageProperties messageProperties = message.getMessageProperties();
        if (body == null) {
            return null;
        }
        try {
            String contentType = (messageProperties != null) ? messageProperties.getContentType() : null;
            if (MessageProperties.CONTENT_TYPE_SERIALIZED_OBJECT.equals(contentType)) {
                return SerializationUtils.deserialize(body).toString();
            }
            if (MessageProperties.CONTENT_TYPE_TEXT_PLAIN.equals(contentType)) {
                return new String(body, ENCODING);
            }
            if (MessageProperties.CONTENT_TYPE_JSON.equals(contentType)) {
                return new String(body, ENCODING);
            }
        } catch (Exception e) {
            // ignore
        }
        return body.toString() + "(byte[" + body.length + "])"; // Comes out as '[B@....b' (so harmless)
    }

    private static Event.EventType extractEventType(MessageProperties messageProperties) throws EventMessageFactoryException {
        String eventTypeString = (String) messageProperties.getHeaders().get(EVENT_MSG_HEADER);

        if (eventTypeString == null)
            throw new EventMessageFactoryException("Wrong event type " + messageProperties);

        return Event.EventType.lookup(Integer.valueOf(eventTypeString));
    }
}
