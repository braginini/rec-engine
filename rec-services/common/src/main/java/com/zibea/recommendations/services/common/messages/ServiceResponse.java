package com.zibea.recommendations.services.common.messages;

import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;

/**
 * @author Mikhail Bragin
 */
public abstract class ServiceResponse extends ServiceMessage {

    protected ServiceResponse(MessageType type) {
        super(type);
    }
}
