package com.zibea.recommendations.services.partner.business.feed.parser;

import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Bragin
 */
public class ParserUtils {

    @NotNull
    public static Element getRequiredElement(String elementName, Element parent) throws DocumentException {

        Element element = getElement(elementName, parent);
        if (element == null)
            throw new DocumentException("required element " + elementName
                    + " was not found in parent element " + parent);

        return element;
    }

    @NotNull
    public static Attribute getRequiredAttribute(String attributeName, Element parent) throws DocumentException {
        Attribute attribute = getAttribute(attributeName, parent);

        if (attribute == null)
            throw new DocumentException("required attribute " + attributeName
                    + " was not found in parent element " + parent);

        return attribute;
    }

    @Nullable
    public static Attribute getAttribute(String attributeName, Element parent) {
        return parent.attribute(attributeName);
    }

    @Nullable
    public static Element getElement(String elementName, Element parent) {
        return parent.element(elementName);
    }

    @NotNull
    public static String getRequiredElementStringValue(String elementName, Element parent) throws DocumentException {
        return getRequiredElement(elementName, parent).getStringValue();
    }

    @NotNull
    public static String getRequiredAttributeStringValue(String attributeName, Element parent) throws DocumentException {
        return getRequiredAttribute(attributeName, parent).getValue();
    }

    @Nullable
    public static String getElementStringValue(String elementName, Element parent) throws DocumentException {
        Element element =  getElement(elementName, parent);

        if (element == null)
            return null;

        return element.getStringValue();
    }


    @Nullable
    public static String getAttributeStringValue(String attributeName, Element parent) throws DocumentException {
        Attribute attribute =  getAttribute(attributeName, parent);

        if (attribute == null)
            return null;

        return attribute.getValue();
    }
}
