package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class OtherProfileModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("ads")
    public ArrayList<AdItemModel> adList;

    @SerializedName("profile_details")
    private UserDetailsModel userDetailsModel;

    @SerializedName("banners")
    ArrayList<BannerItemModel> bannerList;

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

    public ArrayList<AdItemModel> getAdList() {
        return adList;
    }

    public void setAdList(ArrayList<AdItemModel> adList) {
        this.adList = adList;
    }

    public UserDetailsModel getUserDetailsModel() {
        return userDetailsModel;
    }

    public void setUserDetailsModel(UserDetailsModel userDetailsModel) {
        this.userDetailsModel = userDetailsModel;
    }

    public ArrayList<BannerItemModel> getBannerList() {
        return bannerList;
    }

    public void setBannerList(ArrayList<BannerItemModel> bannerList) {
        this.bannerList = bannerList;
    }
}
