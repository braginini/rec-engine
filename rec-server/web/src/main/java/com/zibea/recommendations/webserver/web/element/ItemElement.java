package com.zibea.recommendations.webserver.web.element;

import com.zibea.recommendations.common.model.Item;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author Mikhail Bragin
 */
public class ItemElement {

    @JsonProperty("id")
    private Long id;

    public ItemElement() {
    }

    public ItemElement(Item item) {
        if (item != null)
            this.id = item.getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
