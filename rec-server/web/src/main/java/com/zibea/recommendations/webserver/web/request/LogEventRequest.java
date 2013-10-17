package com.zibea.recommendations.webserver.web.request;

import com.google.common.base.Preconditions;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Mikhail Bragin
 */
public class LogEventRequest extends BaseRequest {

    @NotNull
    @JsonProperty("ts")
    private Long timestamp;

    public LogEventRequest() {
    }

    public LogEventRequest(String apiKey, String ruId, Long timestamp) {
        super(apiKey, ruId);
        this.timestamp = timestamp;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public void validate() {
        super.validate();
        try {
            Preconditions.checkArgument(timestamp != null, "Timestamp field must not be null");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //todo create and throw request exception
        }
    }
}
