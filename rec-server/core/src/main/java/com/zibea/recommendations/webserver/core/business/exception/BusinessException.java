package com.zibea.recommendations.webserver.core.business.exception;

/**
 * @author Mikhail Bragin
 */
public class BusinessException extends Exception {

    public static final String MSG = "Business exception occured";

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
