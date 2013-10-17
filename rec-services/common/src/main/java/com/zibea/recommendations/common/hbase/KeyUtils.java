package com.zibea.recommendations.common.hbase;

/**
 * @author Mikhail Bragin
 */
import java.util.Arrays;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * Utils base class to manipulate keyss
 * @author <a href="mailto:airton@psafe.com">Airton Libório</a>
 */
public class KeyUtils {

    public static int LONG_SIZE = Long.SIZE / Byte.SIZE;
    public static int SHORT_SIZE = Short.SIZE / Byte.SIZE;


    /**
     * Extracts a long value
     * @param row The byte stream
     * @return
     */
    public static Long extractLong(byte[] row) {
        return extractLong(row, 0);
    }

    /**
     * Extracts a long value
     * @param row The byte stream
     * @param offset offset to start the conversion
     * @return
     */
    public static Long extractLong(byte[] row, int offset) {
        if (row == null || row.length < offset + LONG_SIZE)
            return null;
        return Bytes.toLong(row, offset);
    }

    /**
     * Extracts a short value
     * @param row The byte stream
     * @return
     */
    public static Short extractShort(byte[] row) {
        return extractShort(row, 0);
    }

    /**
     * Extracts a short value
     * @param row The byte stream
     * @param offset offset to start the conversion
     * @return
     */
    public static Short extractShort(byte[] row, int offset) {
        if (row == null || row.length < offset + SHORT_SIZE)
            return null;
        return Bytes.toShort(row, offset);
    }

    /**
     * Extracts a long value that is placed reversed and starting at the
     * offset pointer
     * @param row The row
     * @return A {@link Long}, if possible
     */
    public static Long revertExtractLong(byte[] row, int offset) {
        if (row == null || row.length < offset + LONG_SIZE)
            return null;
        return extractLong(revert(Arrays.copyOfRange(row, offset, LONG_SIZE)), 0);
    }

    /**
     * Reverts a byte array
     * TODO: Make this protected
     * @param array
     * @return
     */
    public static byte[] revert(byte[] array) {
        if (array == null) return null;

        byte[] ret = new byte[array.length];
        int i = 0;
        for (byte b : array) {
            ret[array.length - 1 - i] = b;
            i++;
        }
        return ret;
    }

    /** TODO change this name to getKey
     * Gets the first user key for the table
     * @param partnerId The user id
     * @return The byte array HBase key
     */
    public static byte[] getKey(long partnerId) {
        return revert(Bytes.toBytes(partnerId));
    }

    /**
     * Gets the table key imediatelly after the user id. This key could also
     * point to the next user on the table
     * @param partnerId The user id
     * @return The byte array HBase key
     */
    public static byte[] getLastKey(long partnerId) {
        return Bytes.toBytes(Bytes.toLong(revert(Bytes.toBytes(partnerId))) + 1L);
    }

}
