package com.zibea.recommendations.services.common.messages.partner.response;

import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Set;

/**
 * @author Mikhail Bragin
 */
public class PartnerSetResponse extends PartnerResponse {

    @JsonProperty("")
    private Set<Partner> partners;

    public PartnerSetResponse() {
        super(MessageType.PARTNER_SET_RESPONSE);
    }

    public PartnerSetResponse(Set<Partner> partners) {
        this();
        this.partners = partners;
    }

    public Set<Partner> getPartners() {
        return partners;
    }

    public void setPartners(Set<Partner> partners) {
        this.partners = partners;
    }

    public boolean hasPartners() {
        return partners != null && !partners.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PartnerSetResponse");
        sb.append("{partners=").append(partners);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }
}
