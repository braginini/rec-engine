package com.zibea.recommendations.services.common.messages.exception;

/**
 * @author Mikhail Bragin
 */
public class RequestResponseValidationException extends IllegalArgumentException {

    public RequestResponseValidationException(String s) {
        super(s);
    }

    public RequestResponseValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
