package com.zibea.recommendations.webserver.web.response;

import com.zibea.recommendations.common.model.Recommendation;
import com.zibea.recommendations.webserver.web.element.ItemElement;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class RecommendationApiResponse implements IResponse {

    @JsonProperty("recs")
    private List<ItemElement> recommendations;

    public RecommendationApiResponse(List<Recommendation> recommendations) {
        if (recommendations != null && !recommendations.isEmpty()) {
            this.recommendations = new ArrayList<>(recommendations.size());
            for (Recommendation recommendation : recommendations) {
                this.recommendations.add(new ItemElement(recommendation.getRecommendedItem()));
            }
        }
    }

    public List<ItemElement> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<ItemElement> recommendations) {
        this.recommendations = recommendations;
    }
}
