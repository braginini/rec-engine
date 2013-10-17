package com.zibea.recommendations.webserver.core;

import com.zibea.recommendations.webserver.core.business.PartnerInfoActualizer;
import com.zibea.recommendations.webserver.core.dao.impl.EventArchiver;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;
import java.util.concurrent.ThreadFactory;

/**
 * @author Mikhail Bragin
 */

public class Server {

    @Autowired
    EventArchiver eventArchiver;

    @Autowired
    PartnerInfoActualizer partnerActualizer;

    private ThreadPoolTaskScheduler actualizePool;

    private ThreadPoolTaskScheduler archivePool;

    @PostConstruct
    public void init() {

        actualizePool = new ThreadPoolTaskScheduler();
        actualizePool.setThreadFactory(new CustomizableThreadFactory("actualize-pool-worker-"));

        actualizePool.setPoolSize(1);
        actualizePool.initialize();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);

        actualizePool.scheduleWithFixedDelay(partnerActualizer, calendar.getTime(), 60 * 1000 * 10);

        archivePool = new ThreadPoolTaskScheduler();
        archivePool.setThreadFactory(new CustomizableThreadFactory("archive-pool-worker-"));

        archivePool.setPoolSize(1);
        archivePool.initialize();

        archivePool.scheduleWithFixedDelay(eventArchiver, 20000);


    }

    @PreDestroy
    public void destroy() {
        actualizePool.destroy();
        archivePool.destroy();
    }
}
