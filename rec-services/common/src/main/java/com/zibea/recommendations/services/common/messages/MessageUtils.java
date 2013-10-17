package com.zibea.recommendations.services.common.messages;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public class MessageUtils {

    private Multimap<?, ?> collectionMapAsMultiMap(Map<? extends Object, Collection<? extends Object>> map) {
        if (map == null)
            return null;

        Multimap multimap = HashMultimap.create();

        for (Map.Entry<?, Collection<?>> entry : map.entrySet()) {
            multimap.putAll(entry.getKey(), entry.getValue());
        }

        return multimap;
    }
}
