package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeFlyersModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<HomeAdsModel> homeAdsList;


    public ArrayList<HomeAdsModel> getHomeAdsList() {
        return homeAdsList;
    }

    public void setHomeAdsList(ArrayList<HomeAdsModel> homeAdsList) {
        this.homeAdsList = homeAdsList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
