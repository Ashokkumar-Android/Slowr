package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class SubCategoryItemModel {
    @SerializedName("id")
    private String subcategoryId;

    @SerializedName("name")
    private String subcategoryName;

    @SerializedName("slug")
    private String subcategorySlug;



    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName.trim();
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getSubcategorySlug() {
        return subcategorySlug;
    }

    public void setSubcategorySlug(String subcategorySlug) {
        this.subcategorySlug = subcategorySlug;
    }



    public SubCategoryItemModel(String subcategoryId, String subcategoryName, String subcategorySlug) {
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.subcategorySlug = subcategorySlug;
    }
}
