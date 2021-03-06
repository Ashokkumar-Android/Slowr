package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AttributeItemModel {
    @SerializedName("name")
    private String name;
    @SerializedName("attribute_value")
    private ArrayList<AttributesValueItem> attributeValues;

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String attributeId;

    @SerializedName("mandatory")
    private String mandatory;

    @SerializedName("is_title")
    private String isTitle;

    @SerializedName("title_prefix")
    private String isPrefix;

    private String inputValue = "";
    private String inputId = "";
    private String textLength = "";

    private ArrayList<AttributesValueItemTemp> attributeValuesTemp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AttributesValueItem> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(ArrayList<AttributesValueItem> attributeValues) {
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

    public String getIsPrefix() {
        return isPrefix;
    }

    public void setIsPrefix(String isPrefix) {
        this.isPrefix = isPrefix;
    }

    public String getTextLength() {
        return textLength;
    }

    public void setTextLength(String textLength) {
        this.textLength = textLength;
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public ArrayList<AttributesValueItemTemp> getAttributeValuesTemp() {
        return attributeValuesTemp;
    }

    public void setAttributeValuesTemp(ArrayList<AttributesValueItemTemp> attributeValuesTemp) {
        this.attributeValuesTemp = attributeValuesTemp;
    }

    public AttributeItemModel(String name, ArrayList<AttributesValueItem> attributeValues, String type, String attributeId, String mandatory, String isTitle, String isPrefix, String inputValue, String inputId, String txtLength, ArrayList<AttributesValueItemTemp> attributeValuesTemp) {
        this.name = name;
        this.attributeValues = attributeValues;
        this.type = type;
        this.attributeId = attributeId;
        this.mandatory = mandatory;
        this.isTitle = isTitle;
        this.isPrefix = isPrefix;
        this.inputValue = inputValue;
        this.inputId = inputId;
        this.textLength = txtLength;
        this.attributeValuesTemp = attributeValuesTemp;
    }
}