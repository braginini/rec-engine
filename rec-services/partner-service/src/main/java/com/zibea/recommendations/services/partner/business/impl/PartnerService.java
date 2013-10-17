package com.zibea.recommendations.services.partner.business.impl;

import com.google.common.collect.Multimap;
import com.zibea.recommendations.common.api.partnerservice.PartnerServiceGateway;
import com.zibea.recommendations.common.model.*;
import com.zibea.recommendations.services.common.messages.partner.request.CreateUserRequest;
import com.zibea.recommendations.services.common.messages.partner.request.RegisterPartnerRequest;
import com.zibea.recommendations.services.common.messages.partner.request.UpdatePartnerInfoRequest;
import com.zibea.recommendations.services.common.messages.partner.response.PartnerItemUpdateResponse;
import com.zibea.recommendations.services.common.messages.partner.response.RegisterPartnerResponse;
import com.zibea.recommendations.services.common.messages.partner.response.UpdatePartnerInfoResponse;
import com.zibea.recommendations.services.partner.business.IPartnerService;
import com.zibea.recommendations.services.partner.business.feed.FeedActualizer;
import com.zibea.recommendations.services.partner.dao.IPartnerDao;
import com.zibea.recommendations.services.partner.dao.IUtilDao;
import org.apache.commons.lang.RandomStringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

/**
 * @author Mikhail Bragin
 */
public class PartnerService implements IPartnerService {

    private static final int API_KEY_LENGTH = 32;

    protected static final Logger log = LoggerFactory.getLogger(PartnerService.class);

    @Autowired
    IPartnerDao partnerDao;

    @Autowired
    IUtilDao utilDao;

    @Autowired
    FeedActualizer feedActualizer;

    @Autowired
    PartnerServiceGateway partnerGateway;

    @Nullable
    @Override
    public Set<Partner> getPartnerSet() {

        if (log.isDebugEnabled())
            log.debug("Business getPartnerSet started");

        try {
            Set<Partner> allPartners = partnerDao.getPartnerSet();

            if (log.isDebugEnabled())
                log.debug("Business getPartnerSet OK. Returning " + allPartners);

            return allPartners;
        } catch (IOException e) {
            log.error("Error while retrieving partner set");
            return null;
        }
    }

    @NotNull
    @Override
    public PartnerItemUpdateResponse getItemUpdates(long ts, boolean allItems) {

        if (log.isDebugEnabled())
            log.debug("Business getItemUpdates started ts=" + ts);

        try {

            if (allItems) {

                long newTs = System.currentTimeMillis();

                Multimap<Long, Long> itemMap = Item.toLightMap(partnerDao.getAllItems());

                return new PartnerItemUpdateResponse(itemMap, null, newTs);
            }
        } catch (IOException e) {

            log.error("Error while retrieving item updates");

            return new PartnerItemUpdateResponse(System.currentTimeMillis(), null, null);
        }

        //todo finish updates
        return new PartnerItemUpdateResponse(System.currentTimeMillis(), null, null);
    }

    @Deprecated
    @Override
    public void createUser(long partnerId, String ruId, Long partnerUserId) {

        try {

            if (partnerUserId == null)
                partnerDao.createUser(partnerId, ruId);

            else
                partnerDao.createUser(partnerId, ruId, partnerUserId);

        } catch (IOException e) {
            //todo maybe postpone user creation and do it later
            log.error("Error while creating user [partner=" + partnerId + "::ruId=" + ruId +
                    "::partnerUserId=" + partnerUserId + "]");
        }
    }

    @NotNull
    @Override
    public RegisterPartnerResponse registerPartner(RegisterPartnerRequest request) {

        try {
            if (partnerDao.partnerExists(request.getEmail())) {
                //todo send notification to client that email already registered
                return new RegisterPartnerResponse();
            }

            String apiKey = RandomStringUtils.randomAlphanumeric(API_KEY_LENGTH);

            long id = partnerDao.getNextPartnerId();

            Partner partner = new Partner(id, apiKey, true, request.getEmail(), request.getPassword());

            if (partnerDao.createPartner(partner) == null) {
                return new RegisterPartnerResponse(partner.getApiKey(), partner.getId());
            }
        } catch (IOException e) {
            log.error("Error while creating partner " + request, e);
        }

        return new RegisterPartnerResponse();

    }

    @Override
    public void createUser(@NotNull CreateUserRequest request) {
        try {

            if (request.getPartnerUserId() == null)
                partnerDao.createUser(request.getPartnerId(), request.getRuId());

            else
                partnerDao.createUser(request.getPartnerId(), request.getRuId(), request.getPartnerUserId());

        } catch (IOException e) {
            //todo maybe postpone user creation and do it later
            log.error("Error while creating user " + request);
        }
    }

    @NotNull
    @Override
    public UpdatePartnerInfoResponse updatePartnerInfo(UpdatePartnerInfoRequest request) {

        try {

            Partner partner = partnerDao.getPartner(request.getPartnerId());

            if (partner != null) {

                boolean toUpdate = false;

                if (request.getCartUrl() != null && !request.getCartUrl().equals(partner.getCartUrl())) {
                    partner.setCartUrl(request.getCartUrl());
                    toUpdate = true;
                }

                if (request.getYmlUrl() != null && !request.getYmlUrl().equals(partner.getYmlUrl())) {
                    toUpdate = true;
                    partner.setYmlUrl(request.getYmlUrl());
                }

                if (request.getCompanyTitle() != null && !request.getCompanyTitle().equals(partner.getCompanyTitle())) {
                    toUpdate = true;
                    partner.setCompanyTitle(request.getCompanyTitle());
                }

                boolean success = true;

                if (toUpdate)
                    success = partnerDao.updatePartnerInfo(partner);

                partnerGateway.updateFeed(partner.getId());

                if (success)
                    return new UpdatePartnerInfoResponse(success);
            }

        } catch (IOException e) {
            log.error("Error while updating partner info", e);
        }

        return new UpdatePartnerInfoResponse(false);
    }

    @Override
    public void updateFeed(@NotNull Feed feed) {

        if (log.isDebugEnabled())
            log.debug("Business updateFeed " + feed);

        Partner partner = feed.getPartner();

        try {

            updateItems(feed);

            updateCategories(feed);

            partnerDao.updatePartnerFeedUpdateTs(partner, feed.getLastUpdate());

        } catch (IOException e) {
            log.error("Error while updating partner feed in DB", e);
        }

        if (log.isDebugEnabled())
            log.debug("Business updateFeed OK " + feed);
    }

    private void updateItems(Feed feed) throws IOException {

        Partner partner = feed.getPartner();

        Map<String, Item> itemMap = partnerDao.getPartnerItemMap(partner.getId());

        Set<FeedOffer> newOffers = new HashSet<>();
        Set<Item> itemsToUpdate = new HashSet<>();
        Set<Item> itemsToAdd = new HashSet<>();

        for (FeedOffer offer : feed.getOffers()) {

            Item item = itemMap.get(offer.getId());

            if (item == null) {
                newOffers.add(offer);
                continue;
            }

            itemsToUpdate.add(new Item(item.getId(), item.getPartnerId(), offer));
        }

        if (!newOffers.isEmpty()) {

            Queue<Long> newItemIds = utilDao.getNextItemId(partner.getId(), newOffers.size());

            if (newItemIds.size() != newOffers.size()) {
                log.error("Error while updating items. Ids were generated wrongly");
                return;
            }

            for (FeedOffer feedOffer : newOffers) {
                Long id = newItemIds.poll();

                if (id != null)
                    itemsToAdd.add(new Item(id, partner.getId(), feedOffer)); //we add here
                else {
                    log.error("Error while updating items. Can not find id for an item");
                    return;
                }
            }
        }

        partnerDao.updateItems(itemsToUpdate);

        partnerDao.addItems(itemsToAdd);
    }

    private void updateCategories(Feed feed) throws IOException {

        Set<Category> categoriesToAdd = new HashSet<>();
        Set<Category> categoriesToUpdate = new HashSet<>();

        Partner partner = feed.getPartner();

        Map<Long, Category> categoryMap = partnerDao.getPartnerCategoryMap(partner.getId());

        for (Category feedCategory : feed.getCategories()) {

            Category existingCategory = categoryMap.get(feedCategory.getId());

            if (existingCategory != null)
                categoriesToUpdate.add(feedCategory);
            else
                categoriesToAdd.add(feedCategory);

        }

        partnerDao.updateCategories(categoriesToUpdate);

        partnerDao.addCategories(categoriesToAdd);
    }

    @Nullable
    @Override
    public Partner getPartner(long partnerId) {
        try {
            return partnerDao.getPartner(partnerId);
        } catch (IOException e) {
            log.error("Error while getting partner info");
        }
        return null;
    }
}
