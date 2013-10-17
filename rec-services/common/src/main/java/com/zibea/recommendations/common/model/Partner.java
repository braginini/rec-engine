package com.zibea.recommendations.common.model;

/**
 * Represents partner (store) entity
 *
 * @author Mikhail Bragin
 */
public class Partner {

    private long id;

    private String apiKey;

    private boolean isActive = true;

    private String email;

    private String password;

    private String cartUrl;

    private String ymlUrl;

    private String companyTitle;

    private long feedLastUpdateTs;

    public Partner() {
    }

    public Partner(long id, String apiKey) {
        this.id = id;
        this.apiKey = apiKey;
    }

    public Partner(long id, String apiKey, boolean active) {
        this.id = id;
        this.apiKey = apiKey;
        this.isActive = active;
    }

    public Partner(long id, String apiKey, boolean active, String email, String password) {
        this.id = id;
        this.apiKey = apiKey;
        this.isActive = active;
        this.email = email;
        this.password = password;
    }

    public Partner(long id, String apiKey, boolean active, String email, String password,
                   String cartUrl, String ymlUrl, String companyTitle) {
        this.id = id;
        this.apiKey = apiKey;
        this.isActive = active;
        this.email = email;
        this.password = password;
        this.cartUrl = cartUrl;
        this.ymlUrl = ymlUrl;
        this.companyTitle = companyTitle;
    }

    public Partner(long id, String apiKey, boolean active, String email, String password, String cartUrl, String ymlUrl,
                   String companyTitle, long feedLastUpdateTs) {
        this.id = id;
        this.apiKey = apiKey;
        isActive = active;
        this.email = email;
        this.password = password;
        this.cartUrl = cartUrl;
        this.ymlUrl = ymlUrl;
        this.companyTitle = companyTitle;
        this.feedLastUpdateTs = feedLastUpdateTs;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCartUrl() {
        return cartUrl;
    }

    public void setCartUrl(String cartUrl) {
        this.cartUrl = cartUrl;
    }

    public String getYmlUrl() {
        return ymlUrl;
    }

    public void setYmlUrl(String ymlUrl) {
        this.ymlUrl = ymlUrl;
    }

    public String getCompanyTitle() {
        return companyTitle;
    }

    public void setCompanyTitle(String companyTitle) {
        this.companyTitle = companyTitle;
    }

    public long getFeedLastUpdateTs() {
        return feedLastUpdateTs;
    }

    public void setFeedLastUpdateTs(long feedLastUpdateTs) {
        this.feedLastUpdateTs = feedLastUpdateTs;
    }

    /**
     * checks only partner id
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Partner partner = (Partner) o;

        if (id != partner.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Partner");
        sb.append("{id=").append(id);
        sb.append(", apiKey='").append(apiKey).append('\'');
        sb.append(", isActive=").append(isActive);
        sb.append(", email='").append(email).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", cartUrl='").append(cartUrl).append('\'');
        sb.append(", ymlUrl='").append(ymlUrl).append('\'');
        sb.append(", companyTitle='").append(companyTitle).append('\'');
        sb.append(", feedLastUpdateTs=").append(feedLastUpdateTs);
        sb.append('}');
        return sb.toString();
    }
}
