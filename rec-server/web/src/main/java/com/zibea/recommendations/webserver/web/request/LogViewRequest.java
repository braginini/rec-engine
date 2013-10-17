package com.zibea.recommendations.webserver.web.request;

import com.google.common.base.Preconditions;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Mikhail Bragin
 */
public class LogViewRequest extends LogEventRequest {

    @NotNull
    @JsonProperty("itemid")
    private Long itemId;

    public LogViewRequest() {
    }

    public LogViewRequest(String partnerId, String userId, Long itemId, Long timestamp) {
        super(partnerId, userId, timestamp);
        this.itemId = itemId;
    }



    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @Override
    public void validate() {
        super.validate();
        try {
            Preconditions.checkArgument(itemId != null, "Item id field must not be null");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //todo create and throw request exception
        }
    }
}
