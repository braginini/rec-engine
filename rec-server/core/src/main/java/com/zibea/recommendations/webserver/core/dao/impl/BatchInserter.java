package com.zibea.recommendations.webserver.core.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Mikhail Bragin
 */
@Component
@Scope("prototype")
public class BatchInserter {

    private static final Logger log = LoggerFactory.getLogger(BatchInserter.class);

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private Lock r = readWriteLock.readLock();
    private Lock w = readWriteLock.writeLock();

    public void doLockedAdd(Runnable action) {
        r.lock();
        try {
            action.run();
        } finally {
            r.unlock();
        }
    }

    public void flush(InsertAction insertAction) {
        try {
            w.lock();
            boolean success = false;

            while (!success) {
                try {
                    insertAction.flushBatch();

                    success = true;

                    insertAction.createEmptyBatch();
                } catch (Throwable e) {
                    log.error("Error while inserting events to db", e);
                    Thread.sleep(5000);
                }
            }
        } catch (Throwable e) {
            log.error("Error while inserting events to db", e);
        } finally {
            w.unlock();
        }
    }

    public static interface InsertAction {
        void createEmptyBatch();
        void flushBatch() throws IOException;
    }

}
