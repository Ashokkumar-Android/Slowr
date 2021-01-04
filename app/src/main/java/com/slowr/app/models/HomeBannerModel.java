package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeBannerModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<BannerItemModel> bannerList;




    public ArrayList<BannerItemModel> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerItemModel> bannerList) {
        this.bannerList = bannerList;
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
