package com.zibea.recommendations.webserver.core.business.entity;

import java.util.Map;

/**
 * Object represents client parameters
 *
 * @author Mikhail Bragin
 */
public class ParametersEntity {

    private String ruId;

    private String apiKey;

    private Map<String, String> addAttr; //additional attributes used to sent to client

    public ParametersEntity(String apiKey, String ruId) {
        this.ruId = ruId;
        this.apiKey = apiKey;
    }

    public ParametersEntity(String ruId, String apiKey, Map<String, String> addAttr) {
        this.ruId = ruId;
        this.apiKey = apiKey;
        this.addAttr = addAttr;
    }

    public String getRuId() {
        return ruId;
    }

    public void setRuId(String ruId) {
        this.ruId = ruId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public Map<String, String> getAddAttr() {
        return addAttr;
    }

    public void setAddAttr(Map<String, String> addAttr) {
        this.addAttr = addAttr;
    }

    @Override
    public String toString() {
        return "ParametersEntity{" +
                "ruId='" + ruId + '\'' +
                ", apiKey='" + apiKey + '\'' +
                ", addAttr=" + addAttr +
                '}';
    }
}

