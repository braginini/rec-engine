package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;

/**
 * @author Mikhail Bragin
 */
//@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class PartnerItemMapRequest extends PartnerRequest {

    public PartnerItemMapRequest(){
        super(MessageType.PARTNER_ITEM_MAP_REQUEST);
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }
}
