package com.zibea.recommendations.webserver.web.request;

import com.google.common.base.Preconditions;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class LogPurchaseRequest extends LogEventRequest {

    @NotNull
    @JsonProperty("items")
    private List<Long> itemIds;

    public LogPurchaseRequest() {
    }

    public LogPurchaseRequest(String apiKey, String ruId, Long timestamp, List<Long> itemIds) {
        super(apiKey, ruId, timestamp);
        this.itemIds = itemIds;
    }

    public LogPurchaseRequest(String apiKey, String ruId, Long timestamp, String rawItemIds) {
        super(apiKey, ruId, timestamp);
        this.itemIds = splitItems(rawItemIds);
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    @Override
    public void validate() {
       super.validate();
        try {
            Preconditions.checkArgument(itemIds != null, "Items field must not be null");
            Preconditions.checkArgument(!itemIds.isEmpty(), "Items field must not be null");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected List<Long> splitItems(String rawParams) {
        String[] splitItems = rawParams.split(",");
        List<Long> itemIds = new ArrayList<>(splitItems.length);
        for (String stringItem : splitItems) {
            try {
                itemIds.add(Long.valueOf(stringItem));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                //if we have corrupted ids, return null
                return null;
            }
        }

        return itemIds;
    }
}
