package com.zibea.recommendations.services.partner.dao;

import org.apache.hadoop.hbase.client.HTableInterface;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Mikhail Bragin
 */
public abstract class HBaseDao {

    /**
     * HBase connection object
     */
    @Autowired
    public HBaseConnectionPool connectionPool;

    /**
     * Gets {@link org.apache.hadoop.hbase.client.HTableInterface} HBase table from the connection pool
     * {@link HBaseConnectionPool}
     *
     * @param tableName table name to retrieve in <code>byte[]</code> representation
     * @return {@link org.apache.hadoop.hbase.client.HTableInterface} HBase table
     */
    protected HTableInterface getTable(byte[] tableName) {
        return connectionPool.getTable(tableName);
    }
}
