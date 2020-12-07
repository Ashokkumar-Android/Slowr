package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AppVersionModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    AppVersionDataModel appVersionDataModel;

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

    public AppVersionDataModel getAppVersionDataModel() {
        return appVersionDataModel;
    }

    public void setAppVersionDataModel(AppVersionDataModel appVersionDataModel) {
        this.appVersionDataModel = appVersionDataModel;
    }
}
