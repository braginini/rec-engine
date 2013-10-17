package com.zibea.recommendations.common.api.exception;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public class ServiceException extends IOException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
