package com.zibea.recommendations.services.partner.business.feed.parser;

import com.zibea.recommendations.common.model.Category;
import org.dom4j.Attribute;
import org.dom4j.DocumentException;
import org.dom4j.Element;

/**
 * @author Mikhail Bragin
 */
public class CategoryParser {

    public static Category parse(long partnerId, Element element) throws DocumentException {

        try {

            long id = Long.parseLong(ParserUtils.getRequiredAttribute("id", element).getValue());

            Long parentId = null;

            Attribute parentAttr = ParserUtils.getAttribute("parentId", element);
            if (parentAttr != null)
                parentId = Long.parseLong(parentAttr.getValue());

            String title = (element).getStringValue();

            return new Category(id, partnerId, title, parentId);

        } catch (NumberFormatException e) {
            throw new DocumentException("Error while parsing categoryId " + element, e);
        }
    }
}
