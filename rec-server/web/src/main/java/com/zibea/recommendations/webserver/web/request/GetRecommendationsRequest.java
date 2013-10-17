package com.zibea.recommendations.webserver.web.request;

import com.google.common.base.Preconditions;
import com.zibea.recommendations.common.model.Similarity;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * @author Mikhail Bragin
 */
public class GetRecommendationsRequest extends BaseRequest {

    @NotNull
    @JsonProperty("item")
    private Long itemId;

    @NotNull
    @JsonProperty("type")
    private Similarity.RecommendationType type;

    public GetRecommendationsRequest() {
    }

    public GetRecommendationsRequest(Similarity.RecommendationType type,
                                     String apiKey, String ruId, Long itemId) {
        super(apiKey, ruId);
        this.itemId = itemId;
        this.type = type;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Similarity.RecommendationType getType() {
        return type;
    }

    public void setType(Similarity.RecommendationType type) {
        this.type = type;
    }

    @Override
    public void validate() {
        super.validate();
        try {
            Preconditions.checkArgument(itemId != null, "Item id field must not be null");
            Preconditions.checkArgument(type != null, "Type field must not be null");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //todo create and throw request exception
        }
    }
}
