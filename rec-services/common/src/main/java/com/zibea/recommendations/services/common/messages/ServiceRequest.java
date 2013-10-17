package com.zibea.recommendations.services.common.messages;

import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;

/**
 * @author Mikhail Bragin
 */
public abstract class ServiceRequest extends ServiceMessage {

    protected ServiceRequest(MessageType type) {
        super(type);
    }
}
