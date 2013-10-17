package com.zibea.recommendations.common.hbase;

import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author Mikhail Bragin
 */
public class Schema {

    //scan caching
    public static final int SCAN_CACHING = 10000;

    public static final long INC_VALUE = 1l;

    public static final int BYTES_IN_LONG = Long.SIZE / 8;

    public static final int BYTES_IN_SHORT = Short.SIZE / 8;

    // table event row key -> reversed partnerId (long) + ruId (long)
    public static final String TABLE_EVENT_NAME = "event";
    public static final byte[] TABLE_EVENT = Bytes.toBytes(TABLE_EVENT_NAME);

    //family e - event - keeps all user events
    //qualifier -> eventType (short) + eventTimestamp (long)
    //value -> protobuf parser version (1 byte) + event encoded via protobuf parser
    public static final String TABLE_EVENT_FAMILY_ITEM_EVENT_NAME = "e";
    public static final byte[] TABLE_EVENT_FAMILY_ITEM_EVENT = Bytes.toBytes(TABLE_EVENT_FAMILY_ITEM_EVENT_NAME);

    // table user row key -> reversed partnerId (long) + user api key (String length 32)
    //qualifier - user internal id (long)
    //qualifier - user store id (long)
    public static final String TABLE_USER_NAME = "user";
    public static final byte[] TABLE_USER = Bytes.toBytes(TABLE_USER_NAME);

    public static final String TABLE_USER_FAMILY_USER_NAME = "u";
    public static final byte[] TABLE_USER_FAMILY_USER = Bytes.toBytes(TABLE_USER_FAMILY_USER_NAME);

    //user internal id user table qualifier
    public static final String TABLE_USER_QUALIFIER_INTERNAL_ID_NAME = "iid";
    public static final byte[] TABLE_USER_QUALIFIER_INTERNAL_ID = Bytes.toBytes(TABLE_USER_QUALIFIER_INTERNAL_ID_NAME);

    //user store id user table qualifier
    public static final String TABLE_USER_QUALIFIER_STORE_ID_NAME = "sid";
    public static final byte[] TABLE_USER_QUALIFIER_STORE_ID = Bytes.toBytes(TABLE_USER_QUALIFIER_STORE_ID_NAME);


    // table util row key -> reversed partnerId (long)
    public static final String TABLE_UTIL_NAME = "util";
    public static final byte[] TABLE_UTIL = Bytes.toBytes(TABLE_UTIL_NAME);

    //family which keeps the incremental fields (etc userId)
    public static final String TABLE_UTIL_FAMILY_INC_NAME = "inc";
    public static final byte[] TABLE_UTIL_FAMILY_INC = Bytes.toBytes(TABLE_UTIL_FAMILY_INC_NAME);

    //qualifier used to increment user ids
    public static final String TABLE_UTIL_FAMILY_INC_QUALIFIER_USER_ID_NAME = "uid";
    public static final byte[] TABLE_UTIL_FAMILY_INC_QUALIFIER_USER_ID = Bytes.toBytes(TABLE_UTIL_FAMILY_INC_QUALIFIER_USER_ID_NAME);

    //qualifier used to increment user ids
    public static final String TABLE_UTIL_FAMILY_INC_QUALIFIER_ITEM_ID_NAME = "iid";
    public static final byte[] TABLE_UTIL_FAMILY_INC_QUALIFIER_ITEM_ID = Bytes.toBytes(TABLE_UTIL_FAMILY_INC_QUALIFIER_ITEM_ID_NAME);

    // table partner row key -> partner api key
    //qualifier - partner id
    public static final String TABLE_PARTNER_NAME = "partner";
    public static final byte[] TABLE_PARTNER = Bytes.toBytes(TABLE_PARTNER_NAME);

    public static final String TABLE_PARTNER_FAMILY_PARTNER_NAME = "p";
    public static final byte[] TABLE_PARTNER_FAMILY_PARTNER = Bytes.toBytes(TABLE_PARTNER_FAMILY_PARTNER_NAME);

    //qualifier - partner id
    public static final String TABLE_PARTNER_QUALIFIER_ID_NAME = "id";
    public static final byte[] TABLE_PARTNER_QUALIFIER_ID = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_ID_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_EMAIL_NAME = "email";
    public static final byte[] TABLE_PARTNER_QUALIFIER_EMAIL = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_EMAIL_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_PASSWORD_NAME = "pwd";
    public static final byte[] TABLE_PARTNER_QUALIFIER_PASSWORD = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_PASSWORD_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_ACTIVE_NAME = "active";
    public static final byte[] TABLE_PARTNER_QUALIFIER_ACTIVE = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_ACTIVE_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_CART_URL_NAME = "cartUrl";
    public static final byte[] TABLE_PARTNER_QUALIFIER_CART_URL = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_CART_URL_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_YML_URL_NAME = "ymlUrl";
    public static final byte[] TABLE_PARTNER_QUALIFIER_YML_URL = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_YML_URL_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_COMPANY_NAME = "company";
    public static final byte[] TABLE_PARTNER_QUALIFIER_COMPANY = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_COMPANY_NAME);

    public static final String TABLE_PARTNER_QUALIFIER_FEED_UPDATE_TS_NAME = "ts";
    public static final byte[] TABLE_PARTNER_QUALIFIER_FEED_UPDATE_TS = Bytes.toBytes(TABLE_PARTNER_QUALIFIER_FEED_UPDATE_TS_NAME);


    public static final String TABLE_PARTNER_FAMILY_ITEM_NAME = "i";
    public static final byte[] TABLE_PARTNER_FAMILY_ITEM = Bytes.toBytes(TABLE_PARTNER_FAMILY_ITEM_NAME);

    // table recommendation row key -> partner id (reverted) + item internal id
    //qualifiers - similar item ids
    //values - similarity double value
    public static final String TABLE_RECOMMENDATION_NAME = "rec";
    public static final byte[] TABLE_SIMILARITY = Bytes.toBytes(TABLE_RECOMMENDATION_NAME);

    public static final String TABLE_RECOMMENDATION_FAMILY_VIEW_NAME = "iv";
    public static final byte[] TABLE_SIMILARITY_FAMILY_VIEW = Bytes.toBytes(TABLE_RECOMMENDATION_FAMILY_VIEW_NAME);

    // table item row key -> partner id (reverted) + item id in partner store (String)
    public static final String TABLE_ITEM_NAME = "item";
    public static final byte[] TABLE_ITEM = Bytes.toBytes(TABLE_ITEM_NAME);

    public static final String TABLE_ITEM_FAMILY_ITEM_NAME = "i";
    public static final byte[] TABLE_ITEM_FAMILY_ITEM = Bytes.toBytes(TABLE_ITEM_FAMILY_ITEM_NAME);

    //qualifier - item internal id  (long)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_ID_NAME = "id";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_ID = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_ID_NAME);

    //qualifier - item category id (long)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_CATEGORY_ID_NAME = "catid";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_CATEGORY_ID = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_CATEGORY_ID_NAME);

    //qualifier - item is available (boolean)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_AVAILABLE_NAME = "avail";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_AVAILABLE = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_AVAILABLE_NAME);

    //qualifier - item price (double)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_PRICE_NAME = "price";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_PRICE = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_PRICE_NAME);

    //qualifier - item name (string)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_NAME_NAME = "name";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_NAME = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_NAME_NAME);

    //qualifier - item description (string)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_DESCRIPTION_NAME = "descr";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_DESCRIPTION = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_DESCRIPTION_NAME);

    //qualifier - item url  (string)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_URL_NAME = "url";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_URL = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_URL_NAME);

    //qualifier - item additional parameters (protobuf encoded)
    public static final String TABLE_ITEM_QUALIFIER_ITEM_PARAMS_NAME = "params";
    public static final byte[] TABLE_ITEM_QUALIFIER_ITEM_PARAMS = Bytes.toBytes(TABLE_ITEM_QUALIFIER_ITEM_PARAMS_NAME);

    // table category row key -> partner id (reverted) + category id (long)
    public static final String TABLE_CATEGORY_NAME = "cat";
    public static final byte[] TABLE_CATEGORY = Bytes.toBytes(TABLE_CATEGORY_NAME);

    public static final String TABLE_CATEGORY_FAMILY_CATEGORY_NAME = "c";
    public static final byte[] TABLE_CATEGORY_FAMILY_CATEGORY = Bytes.toBytes(TABLE_CATEGORY_FAMILY_CATEGORY_NAME);

    //qualifier - parent id  (long)
    public static final String TABLE_CATEGORY_QUALIFIER_CATEGORY_PARENT_ID_NAME = "pid";
    public static final byte[] TABLE_CATEGORY_QUALIFIER_CATEGORY_PARENT_ID = Bytes.toBytes(TABLE_CATEGORY_QUALIFIER_CATEGORY_PARENT_ID_NAME);

    //qualifier - category name  (string)
    public static final String TABLE_CATEGORY__QUALIFIER_CATEGORY_TITLE_NAME = "n";
    public static final byte[] TABLE_CATEGORY_QUALIFIER_CATEGORY_TITLE = Bytes.toBytes(TABLE_CATEGORY__QUALIFIER_CATEGORY_TITLE_NAME);

}
