package com.zibea.recommendations.services.common.messages.partner.response;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class RegisterPartnerResponse extends PartnerResponse {

    @JsonProperty("")
    private String apiKey;

    @JsonProperty("")
    private Long id;

    public RegisterPartnerResponse(String apiKey, Long id) {
        this();
        this.apiKey = apiKey;
        this.id = id;
    }

    public RegisterPartnerResponse() {
        super(MessageType.REGISTER_PARTNER_RESPONSE);
    }

    public String getApiKey() {
        return apiKey;
    }

    public Long getId() {
        return id;
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }
}
