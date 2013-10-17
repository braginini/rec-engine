package com.zibea.recommendations.common.model;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class FeedOffer {

    private String id;  //id of an item in store

    private long categoryId;

    private boolean available;

    private String url;

    private double price;

    private String name;

    private String description;

    private List<ItemParam> parameters;

    public FeedOffer() {
    }

    public FeedOffer(String id,
                     long categoryId,
                     boolean available,
                     String url,
                     double price,
                     String name,
                     String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.available = available;
        this.url = url;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public static enum OfferType {
        SIMPLE, VENDOR_MODEL, BOOK, AUDIO_BOOK, ARTIST_TITLE, EVENT_TICKET, TOUR;

        public static OfferType lookup(String type) {
            if (type == null)
                return SIMPLE;

            switch (type.toLowerCase()) {
                case "vendor.model":
                    return VENDOR_MODEL;
                case "book":
                    return BOOK;
                case "audiobook":
                    return AUDIO_BOOK;
                case "artist.title":
                    return ARTIST_TITLE;
                case "event-ticket":
                    return EVENT_TICKET;
                case "tour":
                    return TOUR;
                default:
                    return null;
            }

        }


    }
}
