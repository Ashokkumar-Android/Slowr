package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BannerModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<BannerItemModel> bannerItemModelArrayList;

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

    public ArrayList<BannerItemModel> getBannerItemModelArrayList() {
        return bannerItemModelArrayList;
    }

    public void setBannerItemModelArrayList(ArrayList<BannerItemModel> bannerItemModelArrayList) {
        this.bannerItemModelArrayList = bannerItemModelArrayList;
    }
}
