package com.zibea.recommendations.webserver.web.request;

import com.google.common.base.Preconditions;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Mikhail Bragin
 */
public class AssignParamsRequest implements IRequest {

    @NotNull
    @JsonProperty("apikey")
    private String apiKey;

    @JsonProperty("ruId")
    private String ruId;

    public AssignParamsRequest(String partnerId, String userId) {
        this.apiKey = partnerId;
        this.ruId = userId;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void validate() {
        try {
            Preconditions.checkArgument(apiKey != null, "Partner id field must not be null");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //todo create and throw request exception
        }
    }
}
