package com.zibea.recommendations.services.partner.business.feed.parser;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikhail Bragin
 */
public class YandexMarketEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {

        if (systemId.endsWith("shops.dtd")) {
            InputStream in = getClass().getResourceAsStream("/shops.dtd");
            return new InputSource(in);
        }

        return null;
    }
}
