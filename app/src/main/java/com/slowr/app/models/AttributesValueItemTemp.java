package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AttributesValueItemTemp {
    @SerializedName("id")
    String Id;

    @SerializedName("attributeId")
    String attributeId;

    @SerializedName("value")
    String value;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
