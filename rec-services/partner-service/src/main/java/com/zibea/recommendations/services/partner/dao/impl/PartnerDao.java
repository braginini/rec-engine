package com.zibea.recommendations.services.partner.dao.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.zibea.recommendations.common.hbase.KeyUtils;
import com.zibea.recommendations.common.hbase.Schema;
import com.zibea.recommendations.common.model.*;
import com.zibea.recommendations.services.partner.dao.HBaseDao;
import com.zibea.recommendations.services.partner.dao.IPartnerDao;
import com.zibea.recommendations.services.partner.dao.impl.proto.ItemParametersParser;
import com.zibea.recommendations.services.partner.dao.impl.proto.ParameterParserException;
import com.zibea.recommendations.services.partner.dao.util.PartnerDaoUtils;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @author Mikhail Bragin
 */
public class PartnerDao extends HBaseDao implements IPartnerDao {

    private static final int SCAN_CACHING = 1000;

    private static final Logger log = LoggerFactory.getLogger(PartnerDao.class);

    @NotNull
    @Override
    public Set<Partner> getPartnerSet() throws IOException {

        if (log.isDebugEnabled())
            log.debug("Dao getPartnerSet started");

        Scan scan = new Scan()
                .addFamily(Schema.TABLE_PARTNER_FAMILY_PARTNER);

        scan.setCaching(SCAN_CACHING);


        try (HTableInterface table = getTable(Schema.TABLE_PARTNER)) {


            try (ResultScanner scanner = table.getScanner(scan)) {

                Set<Partner> partners = new HashSet<>();

                for (Result result : scanner) {

                    Partner partner = getPartnerValue(result);

                    if (partner != null)
                        partners.add(partner);
                }

                if (log.isDebugEnabled())
                    log.debug("Dao getPartnerSet OK. Returning", partners);

                return partners;
            }
        }
    }

    @NotNull
    @Override
    public Multimap<Long, Item> getAllItems() throws IOException {

        if (log.isDebugEnabled())
            log.debug("Dao getAllItems started");

        Scan scan = new Scan()
                .addFamily(Schema.TABLE_ITEM_FAMILY_ITEM);

        scan.setCaching(SCAN_CACHING);

        try (HTableInterface table = getTable(Schema.TABLE_ITEM)) {

            List<Put> puts = new ArrayList<>();
            for (long i = 0; i < 10; i++) {
                puts.add(new Put(Bytes.add(KeyUtils.revert(Bytes.toBytes(2l)), Bytes.toBytes(i)))
                        .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_ID, Bytes.toBytes(i)));
            }

            for (long i = 0; i < 10; i++) {
                puts.add(new Put(Bytes.add(KeyUtils.revert(Bytes.toBytes(33l)), Bytes.toBytes(i)))
                        .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_ID, Bytes.toBytes(i)));
            }

            table.put(puts);


            try (ResultScanner scanner = table.getScanner(scan)) {

                Multimap<Long, Item> itemMap = HashMultimap.create();

                for (Result result : scanner) {

                    Item item = getSimpleItemValue(result);

                    if (item != null)
                        itemMap.put(item.getPartnerId(), item);
                }

                if (log.isDebugEnabled())
                    log.debug("Dao getAllItems OK. Returning", itemMap);

                return itemMap;
            }
        }
    }

    @Override
    public void createUser(long partnerId, String ruId, Long partnerUserId) throws IOException {

        byte[] rowKey = PartnerDaoUtils.getPartnerIdPlusStringKey(partnerId, ruId);
        byte[] value = (partnerUserId != null) ? Bytes.toBytes(partnerUserId) : new byte[0];

        Put put = new Put(rowKey)
                .add(Schema.TABLE_USER_FAMILY_USER, Schema.TABLE_USER_QUALIFIER_STORE_ID, value);

        try (HTableInterface table = getTable(Schema.TABLE_USER)) {
            table.put(put);
        }

    }

    @Override
    public void createUser(long partnerId, String ruId) throws IOException {
        createUser(partnerId, ruId, null);
    }

    @Override
    public boolean partnerExists(@NotNull String email) throws IOException {

        Scan scan = new Scan()
                .addColumn(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_EMAIL);

        scan.setCaching(1);

        scan.setFilter(new SingleColumnValueFilter(
                Schema.TABLE_PARTNER_FAMILY_PARTNER,
                Schema.TABLE_PARTNER_QUALIFIER_EMAIL,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(email)));

        try (HTableInterface table = getTable(Schema.TABLE_PARTNER)) {
            try (ResultScanner scanner = table.getScanner(scan)) {
                if (scanner.next() != null)
                    return true;
            }
        }

        return false;

    }

    @Override
    public long getNextPartnerId() throws IOException {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Nullable
    @Override
    public Partner createPartner(@NotNull Partner partner) throws IOException {

        Preconditions.checkNotNull(partner.getApiKey());
        Preconditions.checkNotNull(partner.getEmail());
        Preconditions.checkNotNull(partner.getPassword());

        byte[] rowKey = PartnerDaoUtils.getPartnerTableKey(partner.getApiKey());

        Put put = new Put(rowKey)
                .add(Schema.TABLE_PARTNER_FAMILY_PARTNER,
                        Schema.TABLE_PARTNER_QUALIFIER_ID, Bytes.toBytes(partner.getId()))
                .add(Schema.TABLE_PARTNER_FAMILY_PARTNER,
                        Schema.TABLE_PARTNER_QUALIFIER_EMAIL, Bytes.toBytes(partner.getEmail()))
                .add(Schema.TABLE_PARTNER_FAMILY_PARTNER,
                        Schema.TABLE_PARTNER_QUALIFIER_PASSWORD, Bytes.toBytes(partner.getPassword()))
                .add(Schema.TABLE_PARTNER_FAMILY_PARTNER,
                        Schema.TABLE_PARTNER_QUALIFIER_ACTIVE, Bytes.toBytes(true)); //partner.isActive()

        try (HTableInterface table = getTable(Schema.TABLE_PARTNER)) {
            table.put(put);
        }

        return null;
    }

    @Nullable
    @Override
    public Partner getPartner(long partnerId) throws IOException {

        Scan scan = new Scan()
                .addFamily(Schema.TABLE_PARTNER_FAMILY_PARTNER);

        scan.setCaching(1);

        scan.setFilter(new SingleColumnValueFilter(
                Schema.TABLE_PARTNER_FAMILY_PARTNER,
                Schema.TABLE_PARTNER_QUALIFIER_ID,
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(partnerId)));

        try (HTableInterface table = getTable(Schema.TABLE_PARTNER)) {
            try (ResultScanner scanner = table.getScanner(scan)) {
                Result result = scanner.next();

                if (result == null || result.isEmpty())
                    return null;

                return getPartnerValue(result);
            }
        }
    }

    @Override
    public boolean updatePartnerInfo(Partner partner) throws IOException {

        if (partner.getCartUrl() == null && partner.getYmlUrl() == null && partner.getCompanyTitle() == null)
            return true;

        byte[] rowKey = PartnerDaoUtils.getPartnerTableKey(partner.getApiKey());

        Put put = new Put(rowKey);

        if (partner.getCartUrl() != null)
            put.add(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_CART_URL, Bytes.toBytes(partner.getCartUrl()));

        if (partner.getYmlUrl() != null)
            put.add(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_YML_URL, Bytes.toBytes(partner.getYmlUrl()));

        if (partner.getCompanyTitle() != null)
            put.add(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_COMPANY, Bytes.toBytes(partner.getCompanyTitle()));

        try (HTableInterface table = getTable(Schema.TABLE_PARTNER)) {
            table.put(put);
        }

        return true;
    }

    @Override
    public void updateFeed(Feed feed) throws IOException {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @NotNull
    @Override
    public Map<String, Item> getPartnerItemMap(long partnerId) throws IOException {

        byte[] startRowKey = PartnerDaoUtils.getKey(partnerId);
        byte[] stopRowKey = PartnerDaoUtils.getLastKey(partnerId);

        Scan scan = new Scan(startRowKey, stopRowKey)
                .addFamily(Schema.TABLE_ITEM_FAMILY_ITEM);

        scan.setCaching(SCAN_CACHING);

        try (HTableInterface table = getTable(Schema.TABLE_ITEM)) {

            try (ResultScanner scanner = table.getScanner(scan)) {

                Map<String, Item> itemMap = new HashMap();

                for (Result result : scanner) {

                    Item item = getItemValue(result);
                    if (item != null)
                        itemMap.put(item.getStoreItemId(), item);
                }

                return itemMap;
            }
        }
    }

    @Override
    public void updateItems(@NotNull Set<Item> itemsToUpdate) throws IOException {

        List<Put> puts = new ArrayList<>(itemsToUpdate.size());

        for (Item item : itemsToUpdate) {

            byte[] rowKey = PartnerDaoUtils.getPartnerIdPlusStringKey(item.getPartnerId(), item.getStoreItemId());

            Put put = new Put(rowKey)
                    .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_ID,
                            Bytes.toBytes(item.getId()))
                    .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_PRICE,
                            Bytes.toBytes(item.getPrice()))
                    .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_CATEGORY_ID,
                            Bytes.toBytes(item.getCategoryId()))
                    .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_AVAILABLE,
                            Bytes.toBytes(item.isAvailable()))
                    .add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_NAME,
                            Bytes.toBytes(item.getName()));

            if (item.getDescription() != null)
                put.add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_DESCRIPTION,
                        Bytes.toBytes(item.getDescription()));

            if (item.getUrl() != null)
                put.add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_URL,
                        Bytes.toBytes(item.getUrl()));

            if (item.hasParameters())
                try {
                    put.add(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_PARAMS,
                            ItemParametersParser.encode(item.getParameters()));
                } catch (ParameterParserException e) {
                    log.error("Error while parsing item parameters " + item, e);
                }

            puts.add(put);
        }

        try (HTableInterface table = getTable(Schema.TABLE_ITEM)) {
            table.put(puts);
        }
    }

    @Override
    public void updatePartnerFeedUpdateTs(Partner partner, long lastUpdate) throws IOException {
        byte[] rowKey = PartnerDaoUtils.getPartnerTableKey(partner.getApiKey());

        Put put = new Put(rowKey)
                .add(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_FEED_UPDATE_TS,
                        Bytes.toBytes(lastUpdate));

        try (HTableInterface table = getTable(Schema.TABLE_PARTNER)) {
            table.put(put);
        }
    }

    @NotNull
    @Override
    public Map<Long, Category> getPartnerCategoryMap(long partnerId) throws IOException {

        byte[] startRowKey = PartnerDaoUtils.getKey(partnerId);
        byte[] stopRowKey = PartnerDaoUtils.getLastKey(partnerId);

        Scan scan = new Scan(startRowKey, stopRowKey)
                .addFamily(Schema.TABLE_CATEGORY_FAMILY_CATEGORY);

        scan.setCaching(SCAN_CACHING);

        try (HTableInterface table = getTable(Schema.TABLE_CATEGORY)) {

            try (ResultScanner scanner = table.getScanner(scan)) {

                Map<Long, Category> categoryMap = new HashMap();

                for (Result result : scanner) {

                    Category category = getCategoryValue(result);
                    if (category != null)
                        categoryMap.put(category.getId(), category);
                }

                return categoryMap;
            }
        }
    }

    private Category getCategoryValue(Result result) {

        if (log.isDebugEnabled())
            log.debug("Dao getItemValue started");

        if (result == null && result.isEmpty())
            return null;

        Long id = null;
        byte[] rawId = Bytes.tail(result.getRow(), Schema.BYTES_IN_LONG);
        if (rawId != null && rawId.length == Schema.BYTES_IN_LONG)
            id = Bytes.toLong(rawId);

        Long parentId = null;
        byte[] rawParentId = result.getValue(Schema.TABLE_CATEGORY_FAMILY_CATEGORY,
                Schema.TABLE_CATEGORY_QUALIFIER_CATEGORY_PARENT_ID);
        if (rawParentId != null && rawParentId.length == Schema.BYTES_IN_LONG)
            parentId = Bytes.toLong(rawParentId);

        String title = null;
        byte[] rawTitle = result.getValue(Schema.TABLE_CATEGORY_FAMILY_CATEGORY,
                Schema.TABLE_CATEGORY_QUALIFIER_CATEGORY_TITLE);
        if (rawTitle != null)
            title = Bytes.toString(rawTitle);

        Long partnerId = null;
        byte[] rawPartnerId = Bytes.head(result.getRow(), Schema.BYTES_IN_LONG);
        if (rawId != null && rawId.length == Schema.BYTES_IN_LONG)
            partnerId = Bytes.toLong(PartnerDaoUtils.revert(rawPartnerId));

        if (id == null || partnerId == null) {
            log.error("Category data is corrupted", result);
            return null;
        }

        Category category = new Category(id, partnerId, title, parentId);

        if (log.isDebugEnabled())
            log.debug("Dao getCategoryValue returning", category);

        return category;
    }

    @Override
    public void updateCategories(@NotNull Set<Category> categoriesToUpdate) throws IOException {

        List<Put> puts = new ArrayList<>(categoriesToUpdate.size());

        for (Category category : categoriesToUpdate) {

            byte[] rowKey = PartnerDaoUtils.getPartnerIdPlusLongKey(category.getPartnerId(), category.getId());

            Put put = new Put(rowKey)
                    .add(Schema.TABLE_CATEGORY_FAMILY_CATEGORY, Schema.TABLE_CATEGORY_QUALIFIER_CATEGORY_TITLE,
                            Bytes.toBytes(category.getTitle()));

            if (category.getParentId() != null)
                put.add(Schema.TABLE_CATEGORY_FAMILY_CATEGORY, Schema.TABLE_CATEGORY_QUALIFIER_CATEGORY_PARENT_ID,
                    Bytes.toBytes(category.getPartnerId()));

            puts.add(put);
        }

        try (HTableInterface table = getTable(Schema.TABLE_CATEGORY)) {
            table.put(puts);
        }
    }

    @Override
    public void addCategories(@NotNull Set<Category> categoriesToUpdate) throws IOException {
        updateCategories(categoriesToUpdate);
    }

    @Override
    public void addItems(@NotNull Set<Item> itemsToAdd) throws IOException {
        updateItems(itemsToAdd);
    }

    @Nullable
    private Item getItemValue(Result result) {

        Item item = getSimpleItemValue(result);

        String name = null;
        byte[] rawName = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_NAME);
        if (rawName != null)
            name = Bytes.toString(rawName);

        String description = null;
        byte[] rawDescription = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_DESCRIPTION);
        if (rawDescription != null)
            description = Bytes.toString(rawDescription);

        String url = null;
        byte[] rawUrl = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_URL);
        if (rawUrl != null)
            url = Bytes.toString(rawUrl);

        Double price = null;
        byte[] rawPrice = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_PRICE);
        if (rawPrice != null && rawPrice.length == Schema.BYTES_IN_LONG)
            price = Bytes.toDouble(rawPrice);


        if (price == null || name == null) {
            log.error("Item data is corrupted", result);
            return null;
        }

        item.setPrice(price);
        item.setUrl(url);
        item.setName(name);
        item.setDescription(description);

        List<ItemParam> params;
        byte[] rawParams = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_PARAMS);
        if (rawParams != null)
            try {

                params = ItemParametersParser.decode(rawParams);
                item.setParameters(params);

            } catch (Exception e) {
                log.error("Error while parsing item parameters " + item);
            }

        if (log.isDebugEnabled())
            log.debug("Dao getItemValue OK. Returning", item);

        return item;
    }

    @Nullable
    private Item getSimpleItemValue(Result result) {

        if (log.isDebugEnabled())
            log.debug("Dao getItemValue started");

        if (result == null && result.isEmpty())
            return null;

        Long id = null;
        byte[] rawId = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_ID);
        if (rawId != null && rawId.length == Schema.BYTES_IN_LONG)
            id = Bytes.toLong(rawId);

        Long categoryId = null;
        byte[] rawCategoryId = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_CATEGORY_ID);
        if (rawCategoryId != null && rawCategoryId.length == Schema.BYTES_IN_LONG)
            categoryId = Bytes.toLong(rawCategoryId);

        String storeItemId = null;
        byte[] rawStoreItemId = Bytes.tail(result.getRow(), result.getRow().length - Schema.BYTES_IN_LONG);
        if (rawStoreItemId != null)
            storeItemId = Bytes.toString(rawStoreItemId);

        Boolean isAvailable = null;
        byte[] rawIsAvailable = result.getValue(Schema.TABLE_ITEM_FAMILY_ITEM, Schema.TABLE_ITEM_QUALIFIER_ITEM_AVAILABLE);
        if (rawIsAvailable != null && rawIsAvailable.length == 1)
            isAvailable = Bytes.toBoolean(rawIsAvailable);

        Long partnerId = null;
        byte[] rawPartnerId = Bytes.head(result.getRow(), Schema.BYTES_IN_LONG);
        if (rawId != null && rawId.length == Schema.BYTES_IN_LONG)
            partnerId = Bytes.toLong(PartnerDaoUtils.revert(rawPartnerId));


        if (id == null || partnerId == null || categoryId == null || storeItemId == null || isAvailable == null) {
            log.error("Item data is corrupted", result);
            return null;
        }

        Item item = new Item(
                id,
                storeItemId,
                partnerId,
                categoryId,
                isAvailable
        );

        if (log.isDebugEnabled())
            log.debug("Dao getItemValue OK. Returning", item);

        return item;
    }

    @Nullable
    private Partner getPartnerValue(Result result) {

        if (log.isDebugEnabled())
            log.debug("Dao getPartnerValue started");

        if (result == null && result.isEmpty())
            return null;

        Long id = null;
        byte[] rawId = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_ID);
        if (rawId != null && rawId.length == Schema.BYTES_IN_LONG)
            id = Bytes.toLong(rawId);

        String apiKey = null;
        byte[] rawApiKey = result.getRow();
        if (rawApiKey != null && rawId.length > 0)
            apiKey = Bytes.toString(rawApiKey);

        Boolean isActive = null;
        byte[] rawIsActive = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_ACTIVE);
        if (rawIsActive != null && rawIsActive.length == 1)
            isActive = Bytes.toBoolean(rawIsActive);

        String email = null;
        byte[] rawEmail = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_EMAIL);
        if (rawEmail != null && rawEmail.length > 0)
            email = Bytes.toString(rawEmail);

        String pwd = null;
        byte[] rawPwd = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_PASSWORD);
        if (rawPwd != null && rawPwd.length > 0)
            pwd = Bytes.toString(rawPwd);

        String cartUrl = null;
        byte[] rawCartUrl = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_CART_URL);
        if (rawCartUrl != null && rawCartUrl.length > 0)
            cartUrl = Bytes.toString(rawCartUrl);

        String ymlUrl = null;
        byte[] rawYmlUrl = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_YML_URL);
        if (rawYmlUrl != null && rawYmlUrl.length > 0)
            ymlUrl = Bytes.toString(rawYmlUrl);

        String companyTitle = null;
        byte[] rawCompanyTitle = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER, Schema.TABLE_PARTNER_QUALIFIER_COMPANY);
        if (rawCompanyTitle != null && rawCompanyTitle.length > 0)
            companyTitle = Bytes.toString(rawCompanyTitle);

        long feedLastUpdate = 0l;
        byte[] rawFeedLastUpdate = result.getValue(Schema.TABLE_PARTNER_FAMILY_PARTNER,
                Schema.TABLE_PARTNER_QUALIFIER_FEED_UPDATE_TS);
        if (rawFeedLastUpdate != null && rawFeedLastUpdate.length == Schema.BYTES_IN_LONG)
            feedLastUpdate = Bytes.toLong(rawFeedLastUpdate);

        if (id == null || apiKey == null || isActive == null || email == null || pwd == null) {
            log.error("Partner data is corrupted", result);
            return null;
        }

        Partner partner = new Partner(id, apiKey, isActive, email, pwd, cartUrl, ymlUrl, companyTitle, feedLastUpdate);

        if (log.isDebugEnabled())
            log.debug("Dao getPartnerValue OK. Returning", partner);

        return partner;
    }
}
