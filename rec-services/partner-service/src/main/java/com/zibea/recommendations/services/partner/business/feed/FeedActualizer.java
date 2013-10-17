package com.zibea.recommendations.services.partner.business.feed;

import com.zibea.recommendations.common.model.Feed;
import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.services.partner.business.IPartnerService;
import com.zibea.recommendations.services.partner.business.feed.parser.PartnerFeedParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mikhail Bragin
 */
@Component
public class FeedActualizer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(FeedActualizer.class);

    @Autowired
    IPartnerService partnerService;

    private ExecutorService threadPool;

    @PostConstruct
    public void init() {
        threadPool = Executors.newFixedThreadPool(10,
                new CustomizableThreadFactory("feed-actualizer-worker-"));
    }

    @Override
    public void run() {

        try {

            Set<Partner> partners = partnerService.getPartnerSet();

            if (!partners.isEmpty()) {

                for (final Partner partner : partners) {
                    updatePartnerFeed(partner);
                }
            }

        } catch (Throwable e) {
            log.error("Error while actualizing feeds", e);
        }

    }

    public void updatePartnerFeed(final Partner partner) {

        threadPool.submit(new Runnable() {
            @Override
            public void run() {

                try {

                    PartnerFeedParser parser = new PartnerFeedParser(partner);
                    Feed feed = parser.parse();

                    if (feed == null)
                        return;

                    partnerService.updateFeed(feed);

                    log.info("Partner feed updated " + feed);
                } catch (Throwable e) {
                    log.error("Error while updating partner feed", e);
                }
            }
        });

        log.info("Submitted partner feed update" + partner);
    }

}
