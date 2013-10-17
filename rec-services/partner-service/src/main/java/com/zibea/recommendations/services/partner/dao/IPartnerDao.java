package com.zibea.recommendations.services.partner.dao;

import com.google.common.collect.Multimap;
import com.zibea.recommendations.common.model.Category;
import com.zibea.recommendations.common.model.Item;
import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.common.model.Feed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @author Mikhail Bragin
 */
public interface IPartnerDao {

    @NotNull
    Set<Partner> getPartnerSet() throws IOException;

    @NotNull
    Multimap<Long,Item> getAllItems() throws IOException;

    void createUser(long partnerId, String ruId, Long partnerUserId) throws IOException;

    void createUser(long partnerId, String ruId) throws IOException;

    boolean partnerExists(@NotNull String email) throws IOException;

    long getNextPartnerId() throws IOException;

    /**
     * Creates partner all partner fields must be instantiated
     *
     * @param partner
     * @return <code>null</code> if success, otherwise {@link Partner} obj representing already existing partner
     * @throws IOException
     */
    @Nullable
    Partner createPartner(@NotNull Partner partner) throws IOException;

    @Nullable
    Partner getPartner(long partnerId) throws IOException;

    boolean updatePartnerInfo(Partner partner) throws IOException;

    void updateFeed(Feed feed) throws IOException;

    /**
     * Used to get all partner items
     *
     * @param partnerId
     * @return item map, key -> item id in the partner store, value -> {@link Item}
     * @throws IOException
     */
    @NotNull
    public Map<String, Item> getPartnerItemMap(long partnerId) throws IOException;

    void updateItems(@NotNull Set<Item> itemsToUpdate) throws IOException;

    void updatePartnerFeedUpdateTs(Partner partner, long lastUpdate) throws IOException;

    @NotNull
    Map<Long,Category> getPartnerCategoryMap(long partnerId) throws IOException;

    void updateCategories(@NotNull Set<Category> categoriesToUpdate) throws IOException;

    void addCategories(@NotNull Set<Category> categoriesToUpdate) throws IOException;

    void addItems(@NotNull Set<Item> itemsToAdd) throws IOException;
}
