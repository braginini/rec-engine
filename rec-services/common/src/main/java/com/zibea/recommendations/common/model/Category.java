package com.zibea.recommendations.common.model;

/**
 * @author Mikhail Bragin
 */
public class Category {

    private long id;

    private long partnerId;

    private String title;

    private Long parentId;

    public Category(long id, long partnerId, String title, Long parentId) {
        this.id = id;
        this.partnerId = partnerId;
        this.title = title;
        this.parentId = parentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(long partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (id != category.id) return false;
        if (partnerId != category.partnerId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (partnerId ^ (partnerId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Category");
        sb.append("{id=").append(id);
        sb.append(", title='").append(title).append('\'');
        sb.append(", parentId=").append(parentId);
        sb.append('}');
        return sb.toString();
    }
}
