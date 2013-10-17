package com.zibea.recommendations.services.partner;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mikhail Bragin
 */
public class PartnerServiceMain {

    public static void main(String[] args) throws Exception {
        try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("partner-service-context.xml");

            PartnerServiceServer serviceServer = (PartnerServiceServer) context.getBean("partnerServiceServer");
            serviceServer.start();

        } catch (Throwable e) {
            //log.error( "something happend while starting server", e );
            e.printStackTrace();
            System.exit(1);
        }
    }
}
