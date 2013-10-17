package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class PartnerSetRequest extends PartnerRequest {

    @JsonProperty("")
    private String email;

    @JsonProperty("")
    private String password;


    public PartnerSetRequest() {
        super(MessageType.PARTNER_SET_REQUEST);
    }

    public PartnerSetRequest(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    @Override
    public void validate() throws RequestResponseValidationException {
    }
}
