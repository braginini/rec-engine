package com.zibea.recommendations.webserver.core.business.impl;

import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.common.model.event.ItemViewEvent;
import com.zibea.recommendations.common.model.event.PurchaseEvent;
import com.zibea.recommendations.webserver.core.business.IEventService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public abstract class EventService implements IEventService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void logItemViewEvent(@NotNull String apiKey, @NotNull String ruId, long itemId, long timestamp, Map<String, String> attributes) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Business logItemViewEvent started [apiKey=" + apiKey + "::ruId=" + ruId + "]");

        timestamp = System.currentTimeMillis();
        Event event = new ItemViewEvent(apiKey, ruId,
                timestamp, attributes, itemId);

        processEvent(event);

        if (log.isDebugEnabled())
            log.debug("Business logItemViewEvent OK [apiKey=" + apiKey + "::ruid=" + ruId + "]");
    }

    @Override
    public void logPurchaseEvent(String apiKey, String ruId, List<Long> itemIds, Long timestamp, Map<String, String> attributes) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Business logPurchaseEvent started [apiKey=" + apiKey + "::ruId=" + ruId + "]");

        timestamp = System.currentTimeMillis();
        Event event = new PurchaseEvent(apiKey, ruId,
                timestamp, attributes, itemIds);

        processEvent(event);

        if (log.isDebugEnabled())
            log.debug("Business logPurchaseEvent OK [apiKey=" + apiKey + "::ruid=" + ruId + "]");
    }

    /**
     * Used to process an event
     *
     * @param event
     * @throws com.zibea.recommendations.services.common.messages.exception.EventMessageFactoryException
     *
     */
    abstract void processEvent(Event event);
}
