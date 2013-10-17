package com.zibea.recommendations.common.model;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class Recommendation {

    @NotNull
    private Item sourceItem;

    @NotNull
    private Item recommendedItem;

    public Recommendation(Item sourceItem, Item recommendedItem) {
        this.sourceItem = sourceItem;
        this.recommendedItem = recommendedItem;
    }

    public Item getSourceItem() {
        return sourceItem;
    }

    public void setSourceItem(Item sourceItem) {
        this.sourceItem = sourceItem;
    }

    public Item getRecommendedItem() {
        return recommendedItem;
    }

    public void setRecommendedItem(Item recommendedItem) {
        this.recommendedItem = recommendedItem;
    }

    /**
     * Used to create a list of recommendations for source item and corresponding list of recommended items
     *
     * @param sourceItem       {@link Item} object representing the source item to return recommendations for
     * @param recommendedItems list of {@link Item} recommended to specified source item
     * @return the list of {@link Recommendation} objects representing recommendations for a given source item
     */
    public static List<Recommendation> getRecommendations(Item sourceItem, List<Item> recommendedItems) {
        if (sourceItem == null || recommendedItems == null || recommendedItems.isEmpty())
            return Collections.emptyList();

        ArrayList<Recommendation> recommendations = new ArrayList<>(recommendedItems.size());

        for (Item recommendedItem : recommendedItems) {
            recommendations.add(new Recommendation(sourceItem, recommendedItem));
        }

        return recommendations;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Recommendation");
        sb.append("{sourceItem=").append(sourceItem);
        sb.append(", recommendedItem=").append(recommendedItem);
        sb.append('}');
        return sb.toString();
    }
}
