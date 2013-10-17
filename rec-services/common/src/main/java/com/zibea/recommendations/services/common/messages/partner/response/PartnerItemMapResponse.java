package com.zibea.recommendations.services.common.messages.partner.response;

import com.google.common.collect.Multimap;
import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class PartnerItemMapResponse extends PartnerResponse {

    @JsonProperty("")
    private Multimap<Long, Long> partnerItemMap;

    public PartnerItemMapResponse() {
        super(MessageType.PARTNER_ITEM_MAP_RESPONSE);
    }

    public Multimap<Long, Long> getPartnerItemMap() {
        return partnerItemMap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PartnerItemMapResponse");
        sb.append("{partnerItemMap=").append(partnerItemMap);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }

    @Override
    public MessageType getType() {
        return MessageType.PARTNER_ITEM_MAP_RESPONSE;
    }
}
