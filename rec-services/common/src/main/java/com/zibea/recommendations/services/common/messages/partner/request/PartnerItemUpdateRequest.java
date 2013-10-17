package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Used to get item updates. If {@link this#allItems} is set to <code>true</code>
 * then {@link this#ts} field would be ignored and all added items would be returned
 *
 * @author Mikhail Bragin
 */
public class PartnerItemUpdateRequest extends PartnerRequest {

    @JsonProperty("")
    private long ts;

    @JsonProperty("")
    private boolean allItems;

    public PartnerItemUpdateRequest() {
        super(MessageType.PARTNER_ITEM_UPDATE_REQUEST);
    }

    public PartnerItemUpdateRequest(long ts) {
        this();
        this.ts = ts;
    }

    public PartnerItemUpdateRequest(long ts, boolean allItems) {
        this();
        this.ts = ts;
        this.allItems = allItems;
    }

    public long getTs() {
        return ts;
    }

    public boolean isAllItems() {
        return allItems;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PartnerItemUpdateRequest");
        sb.append("{ts=").append(ts);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }
}
