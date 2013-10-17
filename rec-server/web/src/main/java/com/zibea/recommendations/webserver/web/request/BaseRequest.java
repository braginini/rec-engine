package com.zibea.recommendations.webserver.web.request;

import com.google.common.base.Preconditions;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Mikhail Bragin
 */
public abstract class BaseRequest implements IRequest {

    @NotNull
    @JsonProperty("apikey")
    protected String apiKey;

    @NotNull
    @JsonProperty("ruid")
    protected String ruId;

    protected BaseRequest() {
    }

    protected BaseRequest(String apiKey, String ruId) {
        this.apiKey = apiKey;
        this.ruId = ruId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    @Override
    public void validate() {
        Preconditions.checkArgument(apiKey != null, "Partner id field must not be null");
        Preconditions.checkArgument(ruId != null, "User id field must not be null");
    }
}
