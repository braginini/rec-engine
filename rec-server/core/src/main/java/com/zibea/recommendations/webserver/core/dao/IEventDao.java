package com.zibea.recommendations.webserver.core.dao;

import com.zibea.recommendations.common.model.event.Event;

import java.io.IOException;

/**
 * @author Mikhail Bragin
 */
public interface IEventDao {

    void saveEvent(long partnerId, Event event) throws IOException;
}
