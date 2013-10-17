package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class UpdatePartnerInfoRequest extends PartnerRequest {

    @JsonProperty("")
    private long partnerId;

    @JsonProperty("")
    private String companyTitle;

    @JsonProperty("")
    private String cartUrl;

    @JsonProperty("")
    private String ymlUrl;

    public UpdatePartnerInfoRequest(long partnerId, String companyTitle, String cartUrl, String ymlUrl) {
        this();
        this.partnerId = partnerId;
        this.companyTitle = companyTitle;
        this.cartUrl = cartUrl;
        this.ymlUrl = ymlUrl;
    }

    protected UpdatePartnerInfoRequest() {
        super(MessageType.UPDATE_PARTNER_INFO_REQUEST);
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public String getCartUrl() {
        return cartUrl;
    }

    public void setCartUrl(String cartUrl) {
        this.cartUrl = cartUrl;
    }

    public String getYmlUrl() {
        return ymlUrl;
    }

    public void setYmlUrl(String ymlUrl) {
        this.ymlUrl = ymlUrl;
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
