package com.zibea.recommendations.webserver.core.business;

import com.zibea.recommendations.common.api.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mikhail Bragin
 */
@Component
public class PartnerActualizerHelper {

    private static final Logger log = LoggerFactory.getLogger(PartnerActualizerHelper.class);

    private ReentrantLock lock = new ReentrantLock();

    public void actualize(ActualizeAction action) {

        try {
            boolean success = false;

            while (!success) {
                lock.lock();
                try {
                    action.actualizePartnerSet();

                    action.actualizePartnerItemMap();

                    success = true;
                } catch (Throwable e) {
                    log.error("Error while actualizing partner info", e);
                    return;
                }
            }
        } catch (Throwable e) {
            log.error("Error while actualizing partner info", e);
        } finally {
            lock.unlock();
        }

    }

    public static interface ActualizeAction {

        void actualizePartnerSet() throws ServiceException;

        void actualizePartnerItemMap() throws ServiceException;
    }
}
