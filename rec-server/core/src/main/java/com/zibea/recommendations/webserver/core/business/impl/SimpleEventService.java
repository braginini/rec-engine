package com.zibea.recommendations.webserver.core.business.impl;

import com.zibea.recommendations.common.model.event.Event;
import com.zibea.recommendations.webserver.core.business.PartnerInfoActualizer;
import com.zibea.recommendations.webserver.core.dao.IEventDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

/**
 * Processes event and sends it to archiver
 *
 * @author Mikhail Bragin
 */
public class SimpleEventService extends EventService {

    @Autowired
    IEventDao eventDao;

    @Autowired
    PartnerInfoActualizer partnerActualizer;

    @Override
    public void processEvent(Event event) {
        try {
            //no partner registered, skip
            if (!partnerActualizer.hasPartner(event.getApiKey()))
                return;

            long partnerId = partnerActualizer.getPartner(event.getApiKey());
            eventDao.saveEvent(partnerId, event);

        } catch (IOException e) {
            log.error("Error while saving event", e);
        }
    }
}
