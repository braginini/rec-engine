package com.zibea.recommendations.services.common.messages.partner.response;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;

/**
 * @author Mikhail Bragin
 */
public class CreateUserResponse extends PartnerResponse {

    protected CreateUserResponse() {
        super(MessageType.CREATE_USER_RESPONSE);
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }
}
