package com.zibea.recommendations.common.api.partnerservice;

import com.google.common.base.Preconditions;
import com.zibea.recommendations.common.api.exception.ServiceException;
import com.zibea.recommendations.services.common.messages.ServiceMessage;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.support.RabbitGatewaySupport;
import org.springframework.amqp.support.converter.JsonMessageConverter;

/**
 * @author Mikhail Bragin
 */
// T request, E - response
public abstract class ServiceGateway<T extends ServiceMessage, E extends ServiceMessage> extends RabbitGatewaySupport {

    protected static long DEFAULT_REPLY_TIMEOUT = 60 * 1000;

    public ServiceGateway(CachingConnectionFactory connectionFactory, String routingKey) {

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new JsonMessageConverter());
        rabbitTemplate.setRoutingKey(routingKey);
        //rabbitTemplate.setReplyQueue(new Queue(REPLY_QUEUE));
        rabbitTemplate.setReplyTimeout(DEFAULT_REPLY_TIMEOUT);

        setRabbitTemplate(rabbitTemplate);
    }

    public ServiceGateway(CachingConnectionFactory connectionFactory, String routingKey, int replyTimeout) {

        this(connectionFactory, routingKey);
        getRabbitTemplate().setReplyTimeout(replyTimeout);

    }

    @NotNull
    public E sendAndReceive(@NotNull T request) throws ServiceException {

        validateRequest(request);

        try {

            Preconditions.checkNotNull(request);

            E response = (E) getRabbitTemplate().convertSendAndReceive(request);

            validateResponse(response);

            return response;
        } catch (AmqpException e) {
            throw new ServiceException("Error while invoking partner service RPC", e);
        }
    }

    @NotNull
    public void send(@NotNull T request) throws ServiceException {

        validateRequest(request);

        try {

            getRabbitTemplate().convertAndSend(request);

        } catch (AmqpException e) {
            throw new ServiceException("Error while sending request to partner service", e);
        }
    }

    private void validateRequest(T request) throws ServiceException {

        try {

            Preconditions.checkNotNull(request);
            request.validate();

        } catch (NullPointerException e) {
            throw new ServiceException("Request must be instantiated", e);
        } catch (RequestResponseValidationException e) {
            throw new ServiceException("Request validation exception", e);
        }
    }

    private void validateResponse(E response) throws ServiceException {

        try {

            Preconditions.checkNotNull(response);
            response.validate();

        } catch (NullPointerException e) {
            throw new ServiceException("Null response returned. Probably service is unavailable. " +
                    "Check network connection", e);
        } catch (RequestResponseValidationException e) {
            throw new ServiceException("Response validation exception", e);
        }

    }


}
