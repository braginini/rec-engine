package com.zibea.recommendations.services.partner.business.feed.parser;

import com.zibea.recommendations.common.model.Category;
import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.common.model.Feed;
import com.zibea.recommendations.common.model.FeedOffer;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Mikhail Bragin
 */
public class PartnerFeedParser {

    private static final Logger log = LoggerFactory.getLogger(PartnerFeedParser.class);

    private Partner partner;

    public PartnerFeedParser(Partner partner) {
        this.partner = partner;
    }

    @Nullable
    public Feed parse() throws MalformedURLException, DocumentException {

        Document document = getDocument();

        Element ymlCatalogEl = getRootElement(document);

        long ts = getUpdateTimestamp(ymlCatalogEl);

        if (partner.getFeedLastUpdateTs() >= ts)
            return null; //no changes

        Element shopEl = getShopElement(ymlCatalogEl);

        List<Category> categories = getCategories(shopEl);

        List<FeedOffer> items = getItems(shopEl);

        return new Feed(partner, items, categories, ts);
    }

    private List<FeedOffer> getItems(Element shopEl) throws DocumentException {

        Element offersEl = ParserUtils.getRequiredElement("offers", shopEl);

        List<FeedOffer> items = new ArrayList<>(offersEl.elements().size());

        for (Object element : offersEl.elements()) {
            FeedOffer item = OfferParser.parse((Element) element);
            items.add(item);
        }

        return items;
    }

    private List<Category> getCategories(Element shopEl) throws DocumentException {
        Element catsEl = ParserUtils.getRequiredElement("categories", shopEl);

        List<Category> categories = new ArrayList<>(catsEl.elements().size());

        for (Object element : catsEl.elements()) {
            categories.add(CategoryParser.parse(partner.getId(), (Element) element));
        }

        return categories;

    }

    private Element getShopElement(Element ymlCatalogEl) throws DocumentException {

        return ParserUtils.getRequiredElement("shop", ymlCatalogEl);
    }

    private long getUpdateTimestamp(Element ymlCatalogEl) throws DocumentException {
        Attribute dateAttr = ParserUtils.getRequiredAttribute("date", ymlCatalogEl);

        String stringDate = dateAttr.getValue();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;

        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            throw new DocumentException("Error while parsing document update date", e);
        }

        return date.getTime();
    }

    private Element getRootElement(Document document) throws DocumentException {
        Element ymlCatalogEl = document.getRootElement();

        if (ymlCatalogEl == null)
            throw new DocumentException("Root element was not found in document " + document);

        return ymlCatalogEl;
    }

    public Document getDocument() throws DocumentException, MalformedURLException {
        SAXReader reader = new SAXReader();
        reader.setEntityResolver(new YandexMarketEntityResolver());
        Document document = reader.read(new URL(partner.getYmlUrl()));
        return document;
    }


}
