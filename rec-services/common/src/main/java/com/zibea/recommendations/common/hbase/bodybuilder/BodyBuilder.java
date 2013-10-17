package com.zibea.recommendations.common.hbase.bodybuilder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author: Mikhail Bragin
 */
public class BodyBuilder {

        private DataOutputStream dataOutputStream;
        private final ByteArrayOutputStream byteArrayOutputStream;

        public BodyBuilder( ) {
            byteArrayOutputStream = new ByteArrayOutputStream( );
            dataOutputStream = new DataOutputStream( byteArrayOutputStream );
        }

        public BodyBuilder putInt( int value ) throws BodyBuilderException {
            try {
                dataOutputStream.writeInt( Integer.reverseBytes( value ) );
            } catch( IOException e ) {
                throw new BodyBuilderException( "putInt( " + value + ")", e );
            }

            return this;
        }

        public BodyBuilder putShort( short value ) throws BodyBuilderException {
            try {
                dataOutputStream.writeShort( Short.reverseBytes( value ) );
            } catch( IOException e ) {
                throw new BodyBuilderException( "putShort( " + value + ")", e );
            }

            return this;
        }

        public BodyBuilder putByte( byte value ) throws BodyBuilderException {
            try {
                dataOutputStream.writeByte( value );
            } catch( IOException e ) {
                throw new BodyBuilderException( "putByte( " + value + ")", e );
            }

            return this;
        }

        public BodyBuilder putBytes( byte[] value ) throws BodyBuilderException {
            try {
                dataOutputStream.write( value );
            } catch( IOException e ) {
                throw new BodyBuilderException( "putBytes( " + Arrays.toString(value) + ")", e );
            }

            return this;
        }

        public BodyBuilder putLong( long value ) throws BodyBuilderException {
            try {
                dataOutputStream.writeLong( Long.reverseBytes( value ) );
            } catch( IOException e ) {
                throw new BodyBuilderException( "putLong( " + value + ")", e );
            }

            return this;
        }

        public BodyBuilder putZeroString( String value ) throws BodyBuilderException {
            try {
                for( byte b: value.getBytes() ) {
                    dataOutputStream.writeByte( b );
                }

                dataOutputStream.writeByte( 0 );
            } catch( IOException e ) {
                throw new BodyBuilderException( "putZeroString( " + value + " )", e );
            }

            return this;
        }

        public byte[] getBody() {
            return byteArrayOutputStream.toByteArray();
        }
}
