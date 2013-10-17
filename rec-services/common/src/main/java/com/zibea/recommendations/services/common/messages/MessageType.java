package com.zibea.recommendations.services.common.messages;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public enum MessageType {

    EVENT,

    CREATE_USER_REQUEST,

    PARTNER_ITEM_MAP_REQUEST,

    PARTNER_ITEM_UPDATE_REQUEST,

    PARTNER_SET_REQUEST,

    CREATE_USER_RESPONSE,

    PARTNER_ITEM_MAP_RESPONSE,

    PARTNER_ITEM_UPDATE_RESPONSE,

    PARTNER_SET_RESPONSE,

    REGISTER_PARTNER_REQUEST,

    REGISTER_PARTNER_RESPONSE,

    UPDATE_PARTNER_INFO_REQUEST,

    UPDATE_PARTNER_INFO_RESPONSE,

    UPDATE_PARTNER_FEED_REQUEST;

    private static Map<String, MessageType> map = new HashMap<>();

    static {
        for (MessageType messageType : MessageType.values()) {
            map.put(messageType.name().toLowerCase(), messageType);
        }
    }

    public String getType() {
        return this.name().toLowerCase();
    }

    public static MessageType lookup(String type) {
        return map.get(type.toLowerCase());
    }
}
