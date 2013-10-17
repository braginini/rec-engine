package com.zibea.recommendations.services.common.messages;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;

/**
 * @author Mikhail Bragin
 */
public class ServiceMessageJsonMessageConvertor extends JsonMessageConverter {

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        Object obj = super.fromMessage(message);

        if (obj instanceof ServiceMessage) {
            String stringType = message.getMessageProperties().getType();

            if (stringType == null)
                throw new MessageConversionException("Service message should have type field");

            MessageType type = MessageType.lookup(stringType);

            if (type == null)
                throw new MessageConversionException("Unknown message type " + stringType);

            ((ServiceMessage) obj).setType(type);
        }

        return obj;

    }

    @Override
    protected Message createMessage(Object objectToConvert, MessageProperties messageProperties) throws MessageConversionException {
        Message message = super.createMessage(objectToConvert, messageProperties);

        if (objectToConvert instanceof ServiceMessage)
            messageProperties.setType(((ServiceMessage) objectToConvert).getType().getType());

        return message;

    }
}
