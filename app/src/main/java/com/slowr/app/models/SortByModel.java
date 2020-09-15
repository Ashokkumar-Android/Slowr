package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class SortByModel {
    @SerializedName("id")
    private String sortId;
    @SerializedName("value")
    private String sortValue;

    boolean isSelect = false;

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getSortValue() {
        return sortValue;
    }

    public void setSortValue(String sortValue) {
        this.sortValue = sortValue;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
