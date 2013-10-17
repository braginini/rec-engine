package com.zibea.recommendations.services.common.messages.partner.request;

import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class RegisterPartnerRequest extends PartnerRequest {

    @JsonProperty("")
    private String email;

    @JsonProperty("")
    private String password;

    public RegisterPartnerRequest() {
        super(MessageType.REGISTER_PARTNER_REQUEST);
    }

    public RegisterPartnerRequest(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("RegisterPartnerRequest");
        sb.append("{email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
