package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditDataModel {

    @SerializedName("category_type")
    private String catGroup;

    @SerializedName("ad_title_label")
    String categoryTitle;

    @SerializedName("parent_name")
    private String parentTitle;

    @SerializedName("ads")
    private EditAdDetailsModel adDetailsModel;

    @SerializedName("product_types")
    private ArrayList<SubCategoryChildModel> productType;

    @SerializedName("product_type_suggestions")
    private ArrayList<SubCategoryItemModel> productTypeSuggestion;

    @SerializedName("cities")
    private ArrayList<CityItemModel> cityList;

    @SerializedName("localities")
    private ArrayList<AreaItemModel> areaList;

    @SerializedName("rental_duration")
    private String rentalDuration;

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

    @SerializedName("guidelines")
    public ArrayList<String> guideLines;

    @SerializedName("communication")
    CommunicationModel communicationModel;

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

    public CommunicationModel getCommunicationModel() {
        return communicationModel;
    }

    public void setCommunicationModel(CommunicationModel communicationModel) {
        this.communicationModel = communicationModel;
    }

    public ArrayList<SubCategoryItemModel> getProductTypeSuggestion() {
        return productTypeSuggestion;
    }

    public void setProductTypeSuggestion(ArrayList<SubCategoryItemModel> productTypeSuggestion) {
        this.productTypeSuggestion = productTypeSuggestion;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public ArrayList<String> getGuideLines() {
        return guideLines;
    }

    public void setGuideLines(ArrayList<String> guideLines) {
        this.guideLines = guideLines;
    }
}
