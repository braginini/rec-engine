package com.zibea.recommendations.common.model.event;

import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class ItemViewEvent extends Event<Long> {

    public ItemViewEvent() {
    }

    public ItemViewEvent(String apiKey, String ruId,
                         Long timestamp, Map<String, String> attributes, Long parameter) {
        super(EventType.ITEM_VIEW, apiKey, ruId, timestamp, attributes, parameter);
    }
}
