package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class CreateUserRequest extends PartnerRequest {

    @JsonProperty("")
    private long partnerId;

    @JsonProperty("")
    private String ruId;

    @JsonProperty("")
    private Long partnerUserId;

    public CreateUserRequest() {
        super(MessageType.CREATE_USER_REQUEST);
    }

    public CreateUserRequest(long partnerId, String ruId) {
        this();
        this.partnerId = partnerId;
        this.ruId = ruId;
    }

    public CreateUserRequest(long partnerId, String ruId, Long userPartnerId) {
        this();
        this.partnerId = partnerId;
        this.ruId = ruId;
        this.partnerUserId = userPartnerId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public String getRuId() {
        return ruId;
    }

    public Long getPartnerUserId() {
        return partnerUserId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CreateUserRequest");
        sb.append("{partnerId=").append(partnerId);
        sb.append(", ruId='").append(ruId).append('\'');
        sb.append(", partnerUserId=").append(partnerUserId);
        sb.append('}');
        return sb.toString();
    }

    public void validate() throws RequestResponseValidationException {

    }
}
