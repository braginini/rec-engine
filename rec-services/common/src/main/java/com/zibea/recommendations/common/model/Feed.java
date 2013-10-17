package com.zibea.recommendations.common.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class Feed {

    @NotNull
    private Partner partner;

    @NotNull
    private List<FeedOffer> offers;

    @NotNull
    private List<Category> categories;

    private long lastUpdate;

    public Feed(Partner partner, List<FeedOffer> items, List<Category> categories, long ts) {

        this.partner = partner;

        if (items == null)
            items = Collections.emptyList();

        if (categories == null)
            categories = Collections.emptyList();

        this.offers = items;
        this.categories = categories;
        this.lastUpdate = ts;

    }

    public List<FeedOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<FeedOffer> items) {
        this.offers = items;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @NotNull
    public Partner getPartner() {
        return partner;
    }

    public void setPartner(@NotNull Partner partner) {
        this.partner = partner;
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Feed");
        sb.append("{partner=").append(partner);
        sb.append(", offers=").append(offers);
        sb.append(", categories=").append(categories);
        sb.append(", lastUpdate=").append(lastUpdate);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Feed feed = (Feed) o;

        if (!partner.equals(feed.partner)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return partner.hashCode();
    }
}


