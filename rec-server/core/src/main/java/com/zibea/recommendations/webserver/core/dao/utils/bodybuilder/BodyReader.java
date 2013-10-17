package com.zibea.recommendations.webserver.core.dao.utils.bodybuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author: Mikhail Bragin
 */
public class BodyReader {

        private final DataInputStream dataInputStream;

        public BodyReader( byte[] body ) {
            dataInputStream = new DataInputStream( new ByteArrayInputStream( body ) );
        }

        public int readInt( ) throws BodyReaderException {
            try {
                return Integer.reverseBytes( dataInputStream.readInt( ) );
            } catch( IOException e ) {
                throw new BodyReaderException( "readInt()", e );
            }
        }

        public long readLong( ) throws BodyReaderException {
            try {
                return Long.reverseBytes( dataInputStream.readLong( ) );
            } catch( IOException e ) {
                throw new BodyReaderException( "readLong()", e );
            }
        }

        public short readShort( ) throws BodyReaderException {
            try {
                return Short.reverseBytes( dataInputStream.readShort( ) );
            } catch( IOException e ) {
                throw new BodyReaderException( "readShort()", e );
            }
        }

        public byte readByte( ) throws BodyReaderException {
            try {
                return dataInputStream.readByte( );
            } catch( IOException e ) {
                throw new BodyReaderException( "readByte()", e );
            }
        }

        public byte[] readBytes( int count ) throws BodyReaderException {
            byte[] bytes = new byte[ count ];

            try {
                for( int i = 0; i < count; i++ ) {
                    bytes[ i ] = dataInputStream.readByte( );
                }

                return bytes;
            } catch( IOException e ) {
                throw new BodyReaderException( "readBytes(" + count + ")", e );
            }
        }

        public int available() throws BodyReaderException {
            try {
                return dataInputStream.available();
            } catch( IOException e ) {
                throw new BodyReaderException( "available()", e );
            }
        }

        public String readZeroString( ) throws BodyReaderException {
            try {
                byte b;

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( );
                while( ( b = dataInputStream.readByte() ) != 0 ) {
                    byteArrayOutputStream.write( b );
                }

                return new String( byteArrayOutputStream.toByteArray() );
            } catch( IOException e ) {
                throw new BodyReaderException( "readZeorString()", e );
            }
        }
}
