package com.zibea.recommendations.webserver.web.element;

import com.zibea.recommendations.common.model.Recommendation;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class RecommendationElement {

    @JsonProperty("itemA")
    private ItemElement sourceItem;

    @JsonProperty("itemB")
    private ItemElement recommendedItem;

    public RecommendationElement() {
    }

    public RecommendationElement(ItemElement sourceItem, ItemElement recommendedItem) {
        this.sourceItem = sourceItem;
        this.recommendedItem = recommendedItem;
    }

    public RecommendationElement(Recommendation recommendation) {
        if (recommendation != null) {
            this.sourceItem = new ItemElement(recommendation.getSourceItem());
            this.recommendedItem =  new ItemElement(recommendation.getRecommendedItem());
        }
    }

    public ItemElement getSourceItem() {
        return sourceItem;
    }

    public void setSourceItem(ItemElement sourceItem) {
        this.sourceItem = sourceItem;
    }

    public ItemElement getRecommendedItem() {
        return recommendedItem;
    }

    public void setRecommendedItem(ItemElement recommendedItem) {
        this.recommendedItem = recommendedItem;
    }
}
