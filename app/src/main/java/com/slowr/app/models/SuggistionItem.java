package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class SuggistionItem {
    @SerializedName("categoryId")
    String catId = "";
    @SerializedName("subCategoryId")
    String subCatId = "";
    @SerializedName("childCategoryId")
    String childCatId = "";

    @SerializedName("value")
    String searchValue = "";

    @SerializedName("is_prosper")
    String isProsper = "";

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(String subCatId) {
        this.subCatId = subCatId;
    }

    public String getChildCatId() {
        return childCatId;
    }

    public void setChildCatId(String childCatId) {
        this.childCatId = childCatId;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getIsProsper() {
        return isProsper;
    }

    public void setIsProsper(String isProsper) {
        this.isProsper = isProsper;
    }
}
