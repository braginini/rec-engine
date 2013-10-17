package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class UpdateFeedRequest extends PartnerRequest {

    @JsonProperty("")
    private long partnerId;

    protected UpdateFeedRequest() {
        super(MessageType.UPDATE_PARTNER_FEED_REQUEST);
    }

    public UpdateFeedRequest(long partnerId) {
        this();
        this.partnerId = partnerId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }
}
