package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HomeDetailsModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;


    @SerializedName("data")
    private HomeCategoryModel homeDetailsModel;

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

    public HomeCategoryModel getHomeDetailsModel() {
        return homeDetailsModel;
    }

    public void setHomeDetailsModel(HomeCategoryModel homeDetailsModel) {
        this.homeDetailsModel = homeDetailsModel;
    }
}
