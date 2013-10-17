package com.zibea.recommendations.services.common.messages.partner.response;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zibea.recommendations.services.common.messages.MessageType;
import com.zibea.recommendations.services.common.messages.exception.RequestResponseValidationException;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Collection;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class PartnerItemUpdateResponse extends PartnerResponse {

    //key -> partnerId, value -> set of item ids
    @JsonProperty("")
    private Map<Long, Collection<Long>> addedItems;

    @JsonProperty("")
    //key -> partnerId, value -> set of item ids
    private Map<Long, Collection<Long>> removedItems;

    @JsonProperty("")
    private long ts;

    public PartnerItemUpdateResponse() {
        super(MessageType.PARTNER_ITEM_UPDATE_RESPONSE);
    }

    public PartnerItemUpdateResponse(Multimap<Long, Long> addedItems, Multimap<Long, Long> removedItems, long ts) {
        this();
        this.addedItems = toSimpleMap(addedItems);
        this.removedItems = toSimpleMap(removedItems);
        this.ts = ts;
    }

    public PartnerItemUpdateResponse(long ts, Map<Long, Collection<Long>> addedItems, Map<Long, Collection<Long>> removedItems) {
        this();
        this.addedItems = addedItems;
        this.removedItems = removedItems;
        this.ts = ts;
    }

    public Map<Long, Collection<Long>> getAddedItems() {
        return addedItems;
    }

    public Map<Long, Collection<Long>> getRemovedItems() {
        return removedItems;
    }

    public boolean hasAddedItemsUpdate() {
        return addedItems != null && !addedItems.isEmpty();
    }


    public boolean hasRemovedItemsUpdate() {
        return removedItems != null && !removedItems.isEmpty();
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public Multimap<Long, Long> getAddedItemsAsMultiMap() {

        return asLongMultiMap(addedItems);
    }

    public Multimap<Long, Long>  getRemovedItemsAsMultiMap() {

        return asLongMultiMap(removedItems);
    }

    private Multimap<Long, Long> asLongMultiMap(Map<Long, Collection<Long>> map) {
        if (map == null)
            return null;

        Multimap<Long, Long> multimap = HashMultimap.create();

        for (Map.Entry<Long, Collection<Long>> entry : map.entrySet()) {
            multimap.putAll(entry.getKey(), entry.getValue());
        }

        return multimap;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PartnerItemUpdateResponse");
        sb.append("{addedItems=").append(addedItems);
        sb.append(", removedItems=").append(removedItems);
        sb.append(", ts=").append(ts);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void validate() throws RequestResponseValidationException {

    }

    private Map<Long, Collection<Long>> toSimpleMap(Multimap<Long, Long> map) {
        if (map == null || map.isEmpty())
            return null;
        else
            return map.asMap();
    }

    @Override
    public MessageType getType() {
        return MessageType.PARTNER_ITEM_UPDATE_RESPONSE;
    }
}
