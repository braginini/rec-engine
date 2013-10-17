package com.zibea.recommendations.services.eventlog.shutdown;

import org.springframework.stereotype.Component;

@Component
public class ShutdownHook implements Runnable {
    

    @Override
    public void run() {
        try {

            while( true ) {

                sleep();
            }
        } catch( Throwable e ) {
            System.out.println( "something happened while shutting down. See logs" );
            e.printStackTrace();
        }
    }
    
    private void sleep() {
        try {
            Thread.sleep( 2000 );
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
    }

}
