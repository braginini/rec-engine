package com.zibea.recommendations.common.model.event;

import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class PurchaseEvent extends Event<List<Long>> {

    public PurchaseEvent() {
    }

    public PurchaseEvent(String apiKey, String ruId, Long timestamp, Map<String, String> attributes, List<Long> parameter) {
        super(EventType.PURCHASE, apiKey, ruId, timestamp, attributes, parameter);
    }
}
