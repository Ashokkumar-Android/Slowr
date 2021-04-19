
package com.slowr.app.models.Fillter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubcategoryFilterOption {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("ad_title_label")
    @Expose
    private String adTitleLabel;
    @SerializedName("cat_group")
    @Expose
    private String catGroup;
    @SerializedName("rental_duration")
    @Expose
    private String rentalDuration;
    @SerializedName("sub_category")
    @Expose
    private List<SubCategory> subCategory = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdTitleLabel() {
        return adTitleLabel;
    }

    public void setAdTitleLabel(String adTitleLabel) {
        this.adTitleLabel = adTitleLabel;
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

    public List<SubCategory> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(List<SubCategory> subCategory) {
        this.subCategory = subCategory;
    }

}
