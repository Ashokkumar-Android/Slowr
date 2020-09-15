package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AttributeItemModel {
    @SerializedName("name")
    private String name;
    @SerializedName("attribute_values")
    private String attributeValues;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String attributeId;

    @SerializedName("mandatory")
    private String mandatory;

    @SerializedName("is_title")
    private String isTitle;

    private String inputValue = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(String attributeValues) {
        this.attributeValues = attributeValues;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInputValue() {
        return inputValue;
    }

    public void setInputValue(String inputValue) {
        this.inputValue = inputValue;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getIsTitle() {
        return isTitle;
    }

    public void setIsTitle(String isTitle) {
        this.isTitle = isTitle;
    }

    public AttributeItemModel(String name, String attributeValues, String type, String attributeId, String mandatory, String isTitle, String inputValue) {
        this.name = name;
        this.attributeValues = attributeValues;
        this.type = type;
        this.attributeId = attributeId;
        this.mandatory = mandatory;
        this.isTitle = isTitle;
        this.inputValue = inputValue;
    }
}