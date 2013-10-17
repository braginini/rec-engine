package com.zibea.recommendations.webserver.core.business;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.zibea.recommendations.common.api.exception.ServiceException;
import com.zibea.recommendations.common.api.partnerservice.PartnerServiceGateway;
import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.services.common.messages.partner.response.PartnerItemUpdateResponse;
import com.zibea.recommendations.services.common.messages.partner.response.PartnerSetResponse;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mikhail Bragin
 */
public class PartnerInfoActualizer implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PartnerInfoActualizer.class);

    @Autowired
    PartnerServiceGateway partnerService;

    @Autowired
    PartnerActualizerHelper helper;

    private AtomicLong updateTs = new AtomicLong();

    //key -> partner apiKey, value -> partner id
    private Map<String, Long> partnerMap = Collections.synchronizedMap(new HashMap<String, Long>());

    //key -> partner id, value -> partner id
    private Multimap<Long, Long> partnerItemMap =
            Multimaps.synchronizedSetMultimap(HashMultimap.<Long, Long>create());


    @PostConstruct
    public void init() {
        actualize(true);
    }

    /**
     * Runs periodically to load update partner info
     */
    @Override
    public void run() {
        actualize(false);
    }

    /**
     * Used to update local partner info cache
     *
     * @param init flag which indicates whether it is init stage or not
     */
    private void actualize(final boolean init) {

        helper.actualize(new PartnerActualizerHelper.ActualizeAction() {

            @Override
            public void actualizePartnerSet() throws ServiceException {

                PartnerSetResponse response = partnerService.getPartnerSet();

                log.info("Partner set update received " + response);

                if (response.hasPartners()) {

                    partnerMap = Collections.synchronizedMap(new HashMap<String, Long>());

                    for (Partner partner : response.getPartners()) {
                        partnerMap.put(partner.getApiKey(), partner.getId());
                    }

                    log.info("Partners updated to " + partnerMap);
                }
            }

            @Override
            public void actualizePartnerItemMap() throws ServiceException {

                PartnerItemUpdateResponse response = partnerService.getPartnerItemUpdates(updateTs.get(), init);

                log.info("Partner item update received " + response);

                //todo finish item update
                if (response.hasRemovedItemsUpdate()) {

                    for (Map.Entry<Long, Long> entry : response.getRemovedItemsAsMultiMap().entries()) {
                        partnerItemMap.remove(entry.getKey(), entry.getValue());

                    }
                }

                if (init) {
                    //if init stage we can only assign received values of added items to our current
                    partnerItemMap = response.getAddedItemsAsMultiMap();
                } else {

                    if (response.hasAddedItemsUpdate()) {
                        for (Map.Entry<Long, Long> entry : response.getAddedItemsAsMultiMap().entries()) {
                            partnerItemMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                updateTs.set(response.getTs());

                log.info("Partners items updated");
            }
        });
    }

    @Nullable
    public Long getPartner(String apiKey) {
        return partnerMap.get(apiKey);
    }

    /**
     * checks whether partner with specified api key exists
     *
     * @param apiKey
     * @return
     */
    public boolean hasPartner(String apiKey) {
        return partnerMap.get(apiKey) != null;
    }

    /**
     * checks whether partner has item or not
     *
     * @param apiKey
     * @param itemId
     * @return
     */
    public boolean hasItem(String apiKey, long itemId) {
        return true;
        /*Long partner = partnerMap.get(apiKey);
        if (partner == null)
            return false;

        return partnerItemMap.containsEntry(partner, itemId);*/
    }
}
