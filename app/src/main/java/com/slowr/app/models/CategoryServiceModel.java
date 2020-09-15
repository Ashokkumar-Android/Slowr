package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryServiceModel {

    @SerializedName("product")
    private ArrayList<CategoryItemModel> productList;

    @SerializedName("service")
    private ArrayList<CategoryItemModel> serviceList;

    @SerializedName("rentalDurationList")

    private ArrayList<String> rentalDurationList;

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

    public ArrayList<String> getRentalDurationList() {
        return rentalDurationList;
    }

    public void setRentalDurationList(ArrayList<String> rentalDurationList) {
        this.rentalDurationList = rentalDurationList;
    }
}
