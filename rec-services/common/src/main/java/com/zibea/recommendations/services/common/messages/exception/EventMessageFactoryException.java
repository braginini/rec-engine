package com.zibea.recommendations.services.common.messages.exception;

/**
 * @author Mikhail Bragin
 */
public class EventMessageFactoryException extends Exception {

    public EventMessageFactoryException(String message) {
        super(message);
    }

    public EventMessageFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
