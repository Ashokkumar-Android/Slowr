package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategoryItemModel {
    @SerializedName("id")
    private String subcategoryId;

    @SerializedName("name")
    private String subcategoryName;

    @SerializedName("slug")
    private String subcategorySlug;

    @SerializedName("sub_categories")
    private ArrayList<SubCategoryChildModel> subCategoryList;

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

    public ArrayList<SubCategoryChildModel> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategoryChildModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public SubCategoryItemModel(String subcategoryId, String subcategoryName, String subcategorySlug, ArrayList<SubCategoryChildModel> subCategoryList) {
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.subcategorySlug = subcategorySlug;
        this.subCategoryList = subCategoryList;
    }
}
