package com.zibea.recommendations.webserver.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Map;

/**
 *
 * @author Mikhail Bragin
 */
public class AssignParamsResponse implements IResponse {

    @JsonProperty("ruid")
    private String userId;

    @JsonProperty("apikey")
    private String apiKey;

    @JsonProperty("params")
    private Map<String, String> params;

    public AssignParamsResponse() {
    }

    public AssignParamsResponse(String userId) {
       this.userId = userId;
    }

    public AssignParamsResponse(String ruId, String apiKey, Map<String, String> params) {
        this.userId = ruId;
        this.apiKey = apiKey;
        this.params = params;
    }
}
