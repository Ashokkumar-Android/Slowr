package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditDataModel {

    @SerializedName("cat_group")
    private String catGroup;

    @SerializedName("ad_title_label")
    String categoryTitle;

    @SerializedName("SuperParent")
    private String parentTitle;

    @SerializedName("ads")
    private EditAdDetailsModel adDetailsModel;

    @SerializedName("product_types")
    private ArrayList<SubCategoryChildModel> productType;

    @SerializedName("cities")
    private ArrayList<CityItemModel> cityList;

    @SerializedName("localities")
    private ArrayList<AreaItemModel> areaList;

    @SerializedName("rentalDurationList")
    private ArrayList<String> rentalDurationList;

    @SerializedName("photos")
    private ArrayList<String> adImage;

    @SerializedName("attributes_list")
    private AttributeModel attributeModel;

    @SerializedName("user_details")
    private UserDetailsModel userDetailsModel;

    @SerializedName("chat_id")
    String chatId;

    @SerializedName("relatedProduct")
    private ArrayList<AdItemModel> adList;

    @SerializedName("path")
    public String urlPath;

    public String getCatGroup() {
        return catGroup;
    }

    public void setCatGroup(String catGroup) {
        this.catGroup = catGroup;
    }

    public EditAdDetailsModel getAdDetailsModel() {
        return adDetailsModel;
    }

    public void setAdDetailsModel(EditAdDetailsModel adDetailsModel) {
        this.adDetailsModel = adDetailsModel;
    }

    public ArrayList<SubCategoryChildModel> getProductType() {
        return productType;
    }

    public void setProductType(ArrayList<SubCategoryChildModel> productType) {
        this.productType = productType;
    }

    public ArrayList<CityItemModel> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityItemModel> cityList) {
        this.cityList = cityList;
    }

    public ArrayList<AreaItemModel> getAreaList() {
        return areaList;
    }

    public void setAreaList(ArrayList<AreaItemModel> areaList) {
        this.areaList = areaList;
    }

    public ArrayList<String> getRentalDurationList() {
        return rentalDurationList;
    }

    public void setRentalDurationList(ArrayList<String> rentalDurationList) {
        this.rentalDurationList = rentalDurationList;
    }

    public AttributeModel getAttributeModel() {
        return attributeModel;
    }

    public void setAttributeModel(AttributeModel attributeModel) {
        this.attributeModel = attributeModel;
    }

    public ArrayList<String> getAdImage() {
        return adImage;
    }

    public void setAdImage(ArrayList<String> adImage) {
        this.adImage = adImage;
    }

    public String getParentTitle() {
        return parentTitle;
    }

    public void setParentTitle(String parentTitle) {
        this.parentTitle = parentTitle;
    }

    public UserDetailsModel getUserDetailsModel() {
        return userDetailsModel;
    }

    public void setUserDetailsModel(UserDetailsModel userDetailsModel) {
        this.userDetailsModel = userDetailsModel;
    }

    public ArrayList<AdItemModel> getAdList() {
        return adList;
    }

    public void setAdList(ArrayList<AdItemModel> adList) {
        this.adList = adList;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
