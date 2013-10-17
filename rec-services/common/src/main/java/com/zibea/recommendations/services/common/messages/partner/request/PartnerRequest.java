package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.ServiceMessage;

/**
 * @author Mikhail Bragin
 */
public abstract class PartnerRequest extends ServiceMessage {

    protected PartnerRequest(MessageType type) {
        super(type);
    }
}
