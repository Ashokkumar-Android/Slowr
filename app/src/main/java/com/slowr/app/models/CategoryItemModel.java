package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryItemModel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("mobile_icon")
    private String mobile_icon;

    @SerializedName("slug")
    private String slug;

    @SerializedName("cat_group")
    private String catGroup;

    @SerializedName("ad_title_label")
    String categoryTitle;

    @SerializedName("rental_duration")
    String rentalDuration;

    @SerializedName("sub_categories")
    private ArrayList<SubCategoryItemModel> subCategoryList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_icon() {
        return mobile_icon;
    }

    public void setMobile_icon(String mobile_icon) {
        this.mobile_icon = mobile_icon;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<SubCategoryItemModel> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategoryItemModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCatGroup() {
        return catGroup;
    }

    public void setCatGroup(String catGroup) {
        this.catGroup = catGroup;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }
}
