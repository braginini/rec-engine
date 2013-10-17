package com.zibea.recommendations.services.partner.dao.impl;

import com.zibea.recommendations.common.hbase.Schema;
import com.zibea.recommendations.services.partner.dao.HBaseDao;
import com.zibea.recommendations.services.partner.dao.IUtilDao;
import com.zibea.recommendations.services.partner.dao.util.PartnerDaoUtils;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * @author Mikhail Bragin
 */
@Repository
public class UtilDaoImpl extends HBaseDao implements IUtilDao {


    @Override
    public Queue<Long> getNextItemId(long partnerId, int amount) throws IOException {

        byte[] rowKey = PartnerDaoUtils.getKey(partnerId);

        Increment increment = new Increment(rowKey)
                .addColumn(Schema.TABLE_UTIL_FAMILY_INC, Schema.TABLE_UTIL_FAMILY_INC_QUALIFIER_ITEM_ID, amount);

        LinkedList<Long> ids = new LinkedList<>();

        try (HTableInterface table = getTable(Schema.TABLE_UTIL)) {

            Result result = table.increment(increment);

            long lastId = Bytes.toLong(
                    result.getValue(Schema.TABLE_UTIL_FAMILY_INC, Schema.TABLE_UTIL_FAMILY_INC_QUALIFIER_ITEM_ID));

            while (amount > 0) {
                ids.add(lastId);  //the ids order is not critical here, adding to result list
                amount--;
                lastId--;
            }

            return ids;
        }

    }

    @Override
    public long getNextPartnerId() throws IOException {
        return  -1;
    }
}
