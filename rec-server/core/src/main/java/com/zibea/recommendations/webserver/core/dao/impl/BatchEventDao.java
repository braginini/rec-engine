package com.zibea.recommendations.webserver.core.dao.impl;

import com.zibea.recommendations.common.hbase.Schema;
import com.zibea.recommendations.common.hbase.proto.EventParser;
import com.zibea.recommendations.common.hbase.proto.EventParserException;
import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.webserver.core.dao.IEventDao;
import com.zibea.recommendations.webserver.core.dao.utils.EventDaoUtils;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class does not save event directly.
 * It prepares {@link Put} object and sends it to {@link EventArchiver} which
 * collects puts and flushes batch periodically
 *
 * @author Mikhail Bragin
 */
@Repository
public class BatchEventDao extends HBaseDao implements IEventDao {

    private static final Logger log = LoggerFactory.getLogger(BatchEventDao.class);

    @Autowired
    EventArchiver eventArchiver;

    @Override
    public void saveEvent(long partnerId, Event event) throws IOException {

        if (log.isDebugEnabled())
            log.debug("Dao add started [event=" + event + "]");

        try {

            eventArchiver.add(preparePut(partnerId, event));

        } catch (EventParserException e) {
            throw new IOException("Error while encoding event", e);
        }
    }

    @NotNull
    private Put preparePut(long partnerId, Event event) throws EventParserException {

        byte[] rowKey = EventDaoUtils.getKey(partnerId, event.getRuId());

        byte[] qualifier = EventDaoUtils.getEventQualifier((short) event.getType().getType(), event.getTimestamp());

        byte[] value = EventParser.encode(event);

        return new Put(rowKey)
                .add(Schema.TABLE_EVENT_FAMILY_ITEM_EVENT, qualifier, value);

    }

    public List<Event> getEvents(long partnerId) throws IOException {

        List<Event> events = new ArrayList<>();

        byte[] startRow = EventDaoUtils.getKey(partnerId);

        Scan scan = new Scan(startRow);

        scan.setFilter(new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new BinaryPrefixComparator(Bytes.toBytes((short)1))));

        try (HTableInterface table = getTable(Schema.TABLE_EVENT)) {
            try (ResultScanner scanner = table.getScanner(scan)) {
                for (Result result : scanner) {
                    for (KeyValue kv : result.list()) {
                        short type = Bytes.toShort(Bytes.head(kv.getQualifier(), Schema.BYTES_IN_SHORT));

                        String ruId = Bytes.toString(Bytes.tail(kv.getRow(),
                                kv.getRow().length - Schema.BYTES_IN_LONG));

                        long ts = Bytes.toLong(Bytes.tail(kv.getQualifier(), Schema.BYTES_IN_LONG));
                        try {

                            Event event = EventParser.decode(type, null, ruId, ts, kv.getValue());
                            events.add(event);
                        } catch (EventParserException e) {
                            e.printStackTrace();
                        }
                    }
                }


            }
        }
        return events;

    }
}