package com.zibea.recommendations.common.hbase.bodybuilder;

/**
 * @author: Mikhail Bragin
 */
public class BodyReaderException extends Exception {

    public BodyReaderException( ) {
    }

    public BodyReaderException( String message ) {
        super( message );
    }

    public BodyReaderException( String message, Throwable cause ) {
        super( message, cause );
    }

    public BodyReaderException( Throwable cause ) {
        super( cause );
    }
}
