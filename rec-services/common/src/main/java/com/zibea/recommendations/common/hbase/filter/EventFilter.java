package com.zibea.recommendations.common.hbase.filter;

import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.WritableByteArrayComparable;

/**
 * @author Mikhail Bragin
 */
public class EventFilter extends CompareFilter {

    public EventFilter() {
    }

    public EventFilter(CompareOp compareOp, WritableByteArrayComparable comparator) {
        super(compareOp, comparator);
    }

    @Override
    public ReturnCode filterKeyValue(KeyValue v) {
        int qualifierLength = v.getQualifierLength();
        if (qualifierLength > 0) {
            if (doCompare(this.compareOp, this.comparator, v.getBuffer(),
                    v.getQualifierOffset(), qualifierLength)) {
                return ReturnCode.SKIP;
            }
        }
        return ReturnCode.INCLUDE;
    }
}
