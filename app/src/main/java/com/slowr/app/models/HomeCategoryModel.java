package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeCategoryModel {

    @SerializedName("product_catgories")
    private ArrayList<CategoryItemModel> productList;

    @SerializedName("service_catgories")
    private ArrayList<CategoryItemModel> serviceList;

    @SerializedName("cities")
    private ArrayList<CityItemModel> cityList;

    public ArrayList<CategoryItemModel> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<CategoryItemModel> productList) {
        this.productList = productList;
    }

    public ArrayList<CategoryItemModel> getServiceList() {
        return serviceList;
    }

    public void setServiceList(ArrayList<CategoryItemModel> serviceList) {
        this.serviceList = serviceList;
    }

    public ArrayList<CityItemModel> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityItemModel> cityList) {
        this.cityList = cityList;
    }
}
