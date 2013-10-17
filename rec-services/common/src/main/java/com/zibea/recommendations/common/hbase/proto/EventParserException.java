package com.zibea.recommendations.common.hbase.proto;

/**
 * @author Mikhail Bragin
 */
public class EventParserException extends Exception {

    public EventParserException(String message) {
        super(message);
    }

    public EventParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
