package com.zibea.recommendations.common.model;

/**
 * @author Mikhail Bragin
 */
public class User {

    private long id;

    private String ruId;

    private long partnerId;

    public User(long id, String ruId, long partnerId) {
        this.id = id;
        this.ruId = ruId;
        this.partnerId = partnerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
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

        User user = (User) o;

        if (id != user.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", ruId='" + ruId + '\'' +
                ", partnerId=" + partnerId +
                '}';
    }
}
