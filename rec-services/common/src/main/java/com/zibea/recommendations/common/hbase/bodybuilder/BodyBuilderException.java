package com.zibea.recommendations.common.hbase.bodybuilder;

/**
 * @author: Mikhail Bragin
 */
public class BodyBuilderException extends Exception {
    public BodyBuilderException( ) {
    }

    public BodyBuilderException( String message ) {
        super( message );
    }

    public BodyBuilderException( String message, Throwable cause ) {
        super( message, cause );
    }

    public BodyBuilderException( Throwable cause ) {
        super( cause );
    }
}