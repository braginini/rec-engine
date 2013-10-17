package com.zibea.recommendations.services.partner.business.feed.parser;

import com.zibea.recommendations.common.api.exception.ServiceException;
import com.zibea.recommendations.common.api.partnerservice.PartnerServiceGateway;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

/**
 * @author Mikhail Bragin
 */
public class FeedParserTest {

    public static void main(String args[]) throws Exception  {

        if (args.length < 1)
            throw new IllegalArgumentException("Feed url must be specified as an input param");

        /*String url = args[0];

        Partner partner = new Partner();
        partner.setId(-1);
        partner.setYmlUrl(url);
        partner.setFeedLastUpdateTs(0l);

        PartnerFeedParser parser = new PartnerFeedParser(partner);
        Feed feed = parser.parse();*/

        testPartnerUpdateService();

    }

    public static void testPartnerUpdateService() throws ServiceException {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        PartnerServiceGateway gateway = new PartnerServiceGateway(factory, "partner.service.request");
        /*RegisterPartnerResponse  response1 = gateway.registerPartner("b1@gmail.com", "123456");*//*
        UpdatePartnerInfoResponse response = gateway.updatePartnerInfo(1l, "company", "",
                "file:///home/mikhail/shop.xml");*/

        gateway.updateFeed(1l);
        System.exit(0);
    }
}
