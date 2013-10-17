package com.zibea.recommendations.webserver.core;

import com.zibea.recommendations.webserver.core.dao.impl.BatchEventDao;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author Mikhail Bragin
 */
public class DBTest {

    public static void main(String[] arg) throws Exception  {

        BatchEventDao eventDao = new BatchEventDao();
        eventDao.getEvents(2l);

    }
}
