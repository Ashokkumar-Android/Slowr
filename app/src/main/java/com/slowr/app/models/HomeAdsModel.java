package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeAdsModel {

    @SerializedName("title")
    String listTitle;

    @SerializedName("productType")
    String productType;

    @SerializedName("request_flyers")
    String RequestUrl;

    @SerializedName("ads")
    ArrayList<AdItemModel> homeAdsList;

    public String getListTitle() {
        return listTitle;
    }

    public void setListTitle(String listTitle) {
        this.listTitle = listTitle;
    }

    public ArrayList<AdItemModel> getHomeAdsList() {
        return homeAdsList;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public void setHomeAdsList(ArrayList<AdItemModel> homeAdsList) {
        this.homeAdsList = homeAdsList;
    }

    public String getRequestUrl() {
        return RequestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        RequestUrl = requestUrl;
    }
}
