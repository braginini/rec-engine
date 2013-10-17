package com.zibea.recommendations.services.partner.dao.util;

import com.zibea.recommendations.common.hbase.KeyUtils;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author Mikhail Bragin
 */
public class PartnerDaoUtils extends KeyUtils {

    public static byte[] getPartnerTableKey(String apiKey) {
        return Bytes.toBytes(apiKey);
    }

    public static byte[] getPartnerIdPlusStringKey(long partnerId, String string) {
        return Bytes.add(revert(Bytes.toBytes(partnerId)), Bytes.toBytes(string));
    }

    public static byte[] getPartnerIdPlusLongKey(long partnerId, Long longValue) {
        return Bytes.add(revert(Bytes.toBytes(partnerId)), Bytes.toBytes(longValue));
    }
}
