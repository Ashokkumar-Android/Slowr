package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class SubCategoryChildModel {

    @SerializedName("id")
    private String childCategoryId;

    @SerializedName("name")
    private String childCategoryName;

    @SerializedName("slug")
    private String childSlug;

    public String getChildCategoryId() {
        return childCategoryId;
    }

    public void setChildCategoryId(String childCategoryId) {
        this.childCategoryId = childCategoryId;
    }

    public String getChildCategoryName() {
        return childCategoryName;
    }

    public void setChildCategoryName(String childCategoryName) {
        this.childCategoryName = childCategoryName;
    }

    public String getChildSlug() {
        return childSlug;
    }

    public void setChildSlug(String childSlug) {
        this.childSlug = childSlug;
    }
}
