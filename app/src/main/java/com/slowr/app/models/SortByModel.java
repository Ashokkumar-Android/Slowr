package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class SortByModel {
    @SerializedName("id")
    private String sortId;
    @SerializedName("value")
    private String sortValue;

    @SerializedName("action_from")
    private String actionFrom;

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

    public String getActionFrom() {
        return actionFrom;
    }

    public void setActionFrom(String actionFrom) {
        this.actionFrom = actionFrom;
    }

    public SortByModel(String sortId, String sortValue, boolean isSelect) {
        this.sortId = sortId;
        this.sortValue = sortValue;
        this.isSelect = isSelect;
    }
}
