package com.zibea.recommendations.webserver.core.dao.utils;

import com.zibea.recommendations.common.hbase.KeyUtils;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author Mikhail Bragin
 */
public class EventDaoUtils extends KeyUtils {

    public static byte[] getKey(long partnerId, String ruId) {
        return Bytes.add(revert(Bytes.toBytes(partnerId)), Bytes.toBytes(ruId));
    }

    //qualifier consists of eventType and event timestamp
    public static byte[] getEventQualifier(short eventType, long timestamp) {
        return Bytes.add(Bytes.toBytes(eventType), Bytes.toBytes(timestamp));
    }
}
