package com.zibea.recommendations.common.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.codehaus.jackson.annotate.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class Item {

    @JsonProperty("")
    private long id;

    @JsonProperty("")
    private String storeItemId;  //id of an item in store

    @JsonProperty("")
    private long partnerId;

    @JsonProperty("")
    private Long categoryId;

    @JsonProperty("")
    private boolean available;

    @JsonProperty("")
    private String url;

    @JsonProperty("")
    private Double price;

    @JsonProperty("")
    private String name;

    @JsonProperty("")
    private String description;

    @JsonProperty
    private List<ItemParam> parameters;

    public Item() {
    }

    public Item(long id, long partnerId) {
        this.id = id;
        this.partnerId = partnerId;
    }

    public Item(long id,
                String storeItemId,
                long partnerId,
                Long categoryId,
                boolean available,
                String url,
                Double price,
                String name,
                String description,
                List<ItemParam> parameters) {
        this.id = id;
        this.storeItemId = storeItemId;
        this.partnerId = partnerId;
        this.categoryId = categoryId;
        this.available = available;
        this.url = url;
        this.price = price;
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public Item(long id, long partnerId, FeedOffer offer) {

        this(
                id,
                offer.getId(),
                partnerId,
                offer.getCategoryId(),
                offer.isAvailable(),
                offer.getUrl(),
                offer.getPrice(),
                offer.getName(),
                offer.getDescription(),
                offer.getParameters()
        );
    }


    public Item(long id,
                String storeItemId,
                long partnerId,
                Long categoryId,
                boolean available,
                String url,
                Double price,
                String name,
                String description) {
        this.id = id;
        this.storeItemId = storeItemId;
        this.partnerId = partnerId;
        this.categoryId = categoryId;
        this.available = available;
        this.url = url;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    /**
     * Simple representation of an item
     * E.g. fo cache or messaging
     * Don't use without reasonable need
     *
     * @param id
     * @param storeItemId
     * @param partnerId
     * @param categoryId
     * @param available
     */
    public Item(long id,
                String storeItemId,
                long partnerId,
                Long categoryId,
                boolean available) {
        this.id = id;
        this.storeItemId = storeItemId;
        this.partnerId = partnerId;
        this.categoryId = categoryId;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getStoreItemId() {
        return storeItemId;
    }

    public void setStoreItemId(String storeItemId) {
        this.storeItemId = storeItemId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ItemParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<ItemParam> parameters) {
        this.parameters = parameters;
    }

    public boolean hasParameters() {
        return parameters != null && !parameters.isEmpty();
    }

    public static Multimap<Long, Long> toLightMap(@NotNull Multimap<Long, Item> map) {

        Multimap<Long, Long> result = HashMultimap.create();

        for (Map.Entry<Long, Item> entry : map.entries())
            result.put(entry.getKey(), entry.getValue().getId());

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (partnerId != item.partnerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (partnerId ^ (partnerId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Item");
        sb.append("{id=").append(id);
        sb.append(", storeItemId='").append(storeItemId).append('\'');
        sb.append(", partnerId=").append(partnerId);
        sb.append(", categoryId=").append(categoryId);
        sb.append(", available=").append(available);
        sb.append(", url='").append(url).append('\'');
        sb.append(", price=").append(price);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", parameters=").append(parameters);
        sb.append('}');
        return sb.toString();
    }
}
