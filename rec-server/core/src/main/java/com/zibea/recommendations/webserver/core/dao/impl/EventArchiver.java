package com.zibea.recommendations.webserver.core.dao.impl;
import com.zibea.recommendations.common.hbase.Schema;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EventArchiver extends HBaseDao implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(EventArchiver.class);

    @Autowired
    BatchInserter batchInserter;

    private List<Put> batch = Collections.synchronizedList(new ArrayList<Put>()
    );

    AtomicLong size = new AtomicLong();  //the current size of the batch

    AtomicLong total = new AtomicLong();  //the number of all events processed

    @Override
    public void run() {

        batchInserter.flush(new BatchInserter.InsertAction() {

            @Override
            public void createEmptyBatch() {
                log.info("Saved " + size + " events. Total saved " + total);

                size.set(0);
                batch = Collections.synchronizedList(new ArrayList<Put>());
            }

            @Override
            public void flushBatch() throws IOException {
                if (!batch.isEmpty()) {
                    //todo find out why batch is not empty after insert
                    try (HTableInterface table = getTable(Schema.TABLE_EVENT)) {
                        table.put(batch);
                    }
                }
            }
        });

    }

    /**
     * Adds {@link Put} to the batch
     *
     * @param put
     */
    public void add(final Put put) {

        batchInserter.doLockedAdd(
                new Runnable() {
                    public void run() {
                        batch.add(put);
                        size.incrementAndGet();
                        total.incrementAndGet();
                    }
                }
        );
    }
}
