package com.zibea.recommendations.services.common.messages.partner.response;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.ServiceMessage;

/**
 * @author Mikhail Bragin
 */
public abstract class PartnerResponse extends ServiceMessage {

    protected PartnerResponse(MessageType type) {
        super(type);
    }
}
