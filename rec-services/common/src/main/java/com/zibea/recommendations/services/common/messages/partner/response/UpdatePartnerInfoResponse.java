package com.zibea.recommendations.services.common.messages.partner.response;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class UpdatePartnerInfoResponse extends PartnerResponse {

    @JsonProperty("")
    private boolean success;

    protected UpdatePartnerInfoResponse() {
        super(MessageType.UPDATE_PARTNER_INFO_RESPONSE);
    }

    public UpdatePartnerInfoResponse(boolean success) {
        this();
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public void validate() throws RequestResponseValidationException {


    }
}
