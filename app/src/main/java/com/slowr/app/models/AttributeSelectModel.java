package com.slowr.app.models;

public class AttributeSelectModel {
    String id;
    String attributeValue;

    public AttributeSelectModel(String id, String attributeValue) {
        this.id = id;
        this.attributeValue = attributeValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

}
