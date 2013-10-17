package com.zibea.recommendations.common.model.event;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class Event<T>  {

    private EventType type;

    private String apiKey;

    private String ruId;

    private Long timestamp;

    private Map<String, String> attributes;

    private T parameter;

    public Event() {
    }

    public Event(EventType type, String apiKey, String ruId,
                 Long timestamp, Map<String, String> attributes, T parameter) {
        this.type = type;
        this.apiKey = apiKey;
        this.ruId = ruId;
        this.timestamp = timestamp;
        this.attributes = attributes;
        this.parameter = parameter;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Event");
        sb.append("{type=").append(type);
        sb.append(", apiKey='").append(apiKey).append('\'');
        sb.append(", ruId='").append(ruId).append('\'');
        sb.append(", timestamp=").append(timestamp);
        sb.append(", attributes=").append(attributes);
        sb.append(", parameter=").append(parameter);
        sb.append('}');
        return sb.toString();
    }

    public enum EventType {

        ITEM_VIEW(1), PURCHASE(2);

        private int type;

        private static Map<Integer, EventType> map = new HashMap<>();

        static {
            for (EventType messageType : EventType.values()) {
                map.put(messageType.getType(), messageType);
            }
        }

        private EventType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static EventType lookup(int type) {
            return map.get(type);
        }
    }
}
