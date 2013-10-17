package com.zibea.recommendations.common.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Bragin
 */
public class ItemParam {

    @JsonProperty("")
    private String name;

    @JsonProperty("")
    @Nullable
    private String unit;

    @JsonProperty("")
    private String value;

    public ItemParam() {
    }

    public ItemParam(String name, String unit, String value) {
        this.name = name;
        this.unit = unit;
        this.value = value;
    }

    public ItemParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean hasUnit() {
        return unit != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemParam itemParam = (ItemParam) o;

        if (name != null ? !name.equals(itemParam.name) : itemParam.name != null) return false;
        if (unit != null ? !unit.equals(itemParam.unit) : itemParam.unit != null) return false;
        if (value != null ? !value.equals(itemParam.value) : itemParam.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ItemParam");
        sb.append("{name='").append(name).append('\'');
        sb.append(", unit='").append(unit).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

