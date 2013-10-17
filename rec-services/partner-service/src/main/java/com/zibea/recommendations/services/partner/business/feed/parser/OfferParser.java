package com.zibea.recommendations.services.partner.business.feed.parser;

import com.zibea.recommendations.common.model.ItemParam;
import com.zibea.recommendations.common.model.FeedOffer;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class OfferParser {

    private static final Logger log = LoggerFactory.getLogger(OfferParser.class);

    public static FeedOffer parse(Element element) throws DocumentException {

        //common fields
        String id = ParserUtils.getRequiredAttributeStringValue("id", element);
        Boolean available = getAvailability(element);
        String url = ParserUtils.getElementStringValue("url", element);
        Double price = getPrice(element);
        long category = getCategory(element);
        String description = ParserUtils.getElementStringValue("description", element);

        //todo add enum here by type
        String name = getName(element);

        FeedOffer offer = new FeedOffer(id, category, available, url, price, name, description);

        List<ItemParam> params = new ArrayList<>();

        addParam("vendor", ParserUtils.getElementStringValue("vendor", element), null, params);
        addParam("vendorCode", ParserUtils.getElementStringValue("vendorCode", element), null, params);
        addParam("country_of_origin", ParserUtils.getElementStringValue("country_of_origin", element), null, params);
        addParam("typePrefix", ParserUtils.getElementStringValue("typePrefix", element), null, params);
        addParam("publisher", ParserUtils.getElementStringValue("publisher", element), null, params);
        addParam("series", ParserUtils.getElementStringValue("series", element), null, params);
        addParam("author", ParserUtils.getElementStringValue("author", element), null, params);
        addParam("year", ParserUtils.getElementStringValue("year", element), null, params);
        addParam("ISBN", ParserUtils.getElementStringValue("ISBN", element), null, params);
        addParam("volume", ParserUtils.getElementStringValue("volume", element), null, params);
        addParam("part", ParserUtils.getElementStringValue("part", element), null, params);
        addParam("language", ParserUtils.getElementStringValue("language", element), null, params);
        addParam("binding", ParserUtils.getElementStringValue("binding", element), null, params);
        addParam("page_extent", ParserUtils.getElementStringValue("page_extent", element), null, params);
        addParam("table_of_contents", ParserUtils.getElementStringValue("table_of_contents", element), null, params);
        addParam("perfomed_by", ParserUtils.getElementStringValue("perfomed_by", element), null, params);
        addParam("perfomance_type", ParserUtils.getElementStringValue("perfomance_type", element), null, params);
        addParam("storage", ParserUtils.getElementStringValue("storage", element), null, params);
        addParam("format", ParserUtils.getElementStringValue("format", element), null, params);
        addParam("recording_length", ParserUtils.getElementStringValue("recording_length", element), null, params);
        addParam("artist", ParserUtils.getElementStringValue("artist", element), null, params);
        addParam("media", ParserUtils.getElementStringValue("media", element), null, params);
        addParam("currencyId", ParserUtils.getElementStringValue("currencyId", element), null, params);
        addParam("place", ParserUtils.getElementStringValue("place", element), null, params);
        addParam("country", ParserUtils.getElementStringValue("country", element), null, params);
        addParam("worldRegion", ParserUtils.getElementStringValue("worldRegion", element), null, params);
        addParam("region", ParserUtils.getElementStringValue("region", element), null, params);
        addParam("days", ParserUtils.getElementStringValue("days", element), null, params);
        addParam("dataTour", ParserUtils.getElementStringValue("dataTour", element), null, params);
        addParam("hotel_stars", ParserUtils.getElementStringValue("hotel_stars", element), null, params);
        addParam("room", ParserUtils.getElementStringValue("room", element), null, params);
        addParam("meal", ParserUtils.getElementStringValue("meal", element), null, params);
        addParam("included", ParserUtils.getElementStringValue("included", element), null, params);
        addParam("transport", ParserUtils.getElementStringValue("transport", element), null, params);
        addParam("hall", ParserUtils.getElementStringValue("hall", element), null, params);
        addParam("hall_part", ParserUtils.getElementStringValue("hall_part", element), null, params);
        addParam("date", ParserUtils.getElementStringValue("date", element), null, params);

        params.addAll(getParams(element));

        offer.setParameters(params);

        return offer;
    }

    private static void addParam(String name, String value, String unit, List<ItemParam> params) {

        if (value != null)
            params.add(new ItemParam(name, unit, value));
    }

    private static String getName(Element element) throws DocumentException {
        String name = ParserUtils.getElementStringValue("name", element);

        if (name == null)   //for vendor.model
            name = ParserUtils.getElementStringValue("model", element);

        if (name == null) //for music
            name = ParserUtils.getRequiredElementStringValue("title", element);

        return name;
    }

    private static List<ItemParam> getParams(Element element) throws DocumentException {
        List paramEls = element.elements("param");

        List<ItemParam> params = new ArrayList<>(paramEls.size());
        for (Object el : paramEls) {
            String name = ParserUtils.getRequiredAttributeStringValue("name", (Element) el);
            String unit = ParserUtils.getAttributeStringValue("unit", (Element) el);
            String value = ((Element) el).getStringValue();

            params.add(new ItemParam(name, unit, value));
        }

        return params;
    }

    private static long getCategory(Element element) throws DocumentException {

        String str = ParserUtils.getRequiredElementStringValue("categoryId", element);

        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            throw new DocumentException("Error parsing offer category id " + element, e);
        }

    }

    private static Double getPrice(Element element) throws DocumentException {
        String priceStr = ParserUtils.getRequiredElementStringValue("price", element);
        try {
            return Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            throw new DocumentException("Error parsing offer price " + element, e);
        }
    }

    private static Boolean getAvailability(Element element) throws DocumentException {
        String availableStr = ParserUtils.getAttributeStringValue("available", element);

        if (availableStr == null)
            return true;

        try {
            return Boolean.parseBoolean(availableStr);
        } catch (NumberFormatException e) {
            return true;
        }
    }

}
