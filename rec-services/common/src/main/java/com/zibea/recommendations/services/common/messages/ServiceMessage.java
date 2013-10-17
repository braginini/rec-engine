package com.zibea.recommendations.services.common.messages;

import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public abstract class ServiceMessage {

    @JsonProperty("")
    private MessageType type;

    protected ServiceMessage() {
    }

    protected ServiceMessage(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
       return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public abstract void validate() throws RequestResponseValidationException;
}
