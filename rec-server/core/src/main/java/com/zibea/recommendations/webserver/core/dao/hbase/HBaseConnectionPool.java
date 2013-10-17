package com.zibea.recommendations.webserver.core.dao.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.util.PoolMap;
import org.springframework.context.annotation.Scope;

@Scope(value = "singleton")
public class HBaseConnectionPool {
    private final HTablePool htablePool;
    private final Configuration config;

    public HBaseConnectionPool(String connectionUrl) {
        config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", connectionUrl);
        htablePool = new HTablePool(config, Integer.MAX_VALUE, PoolMap.PoolType.Reusable);
    }

    public HTableInterface getTable(byte[] tableName) {
        return htablePool.getTable(tableName);
    }

    public Configuration getConfig() {
        return config;
    }
}
