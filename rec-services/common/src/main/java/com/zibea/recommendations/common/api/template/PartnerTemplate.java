package com.zibea.recommendations.common.api.template;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author Mikhail Bragin
 */
public class PartnerTemplate extends RabbitTemplate {

    private PartnerTemplate(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    private PartnerTemplate(String host, String username, String password) {
        setConnectionFactory(getConnectionFactory(host, username, password));
    }

    public static PartnerTemplate create(String host, String username, String password) {
        return new PartnerTemplate(getConnectionFactory(host, username, password));
    }

    private static ConnectionFactory getConnectionFactory(String host, String username, String password) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
        connectionFactory.setPassword(password);
        connectionFactory.setUsername(username);
        return connectionFactory;
    }
}
