package com.zibea.recommendations.services.partner.dao;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Mikhail Bragin
 */
public interface IUtilDao {

    /**
     * Used to get a set of ids
     *
     * @param partnerId
     * @return
     * @throws IOException
     */
    public Queue<Long> getNextItemId(long partnerId, int amount) throws IOException;

    public long getNextPartnerId() throws IOException;
}
