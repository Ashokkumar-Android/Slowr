package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AttributeModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private ArrayList<AttributeItemModel> attributeList;

    @SerializedName("attribute")
    private ArrayList<AttributeItemModel> attributeEditList;

    @SerializedName("ads_value")
    private ArrayList<AttributesValueItem> attributeValues;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<AttributeItemModel> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(ArrayList<AttributeItemModel> attributeList) {
        this.attributeList = attributeList;
    }

    public ArrayList<AttributeItemModel> getAttributeEditList() {
        return attributeEditList;
    }

    public void setAttributeEditList(ArrayList<AttributeItemModel> attributeEditList) {
        this.attributeEditList = attributeEditList;
    }

    public ArrayList<AttributesValueItem> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(ArrayList<AttributesValueItem> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
