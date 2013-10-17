package com.zibea.recommendations.webserver.core.business;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public interface IEventService {

    /**
     * Logs item view event
     *
     * @param apiKey     the api key of a partner
     * @param ruId       the user unique string id
     * @param itemId     the partner store item id
     * @param timestamp  the action timestamp
     * @param attributes additional attributes
     * @throws Exception
     */
    void logItemViewEvent(@NotNull String apiKey, @NotNull String ruId, long itemId, long timestamp,
                          Map<String, String> attributes) throws Exception;

    /**
     * Logs purchase event
     *
     * @param apiKey     the api key of a partner
     * @param ruId       the user unique string id
     * @param itemIds    the partner store items ids
     * @param timestamp  the action timestamp
     * @param attributes additional attributes
     * @throws Exception
     */
    void logPurchaseEvent(String apiKey, String ruId, List<Long> itemIds,
                          Long timestamp, Map<String, String> attributes) throws Exception;
}
