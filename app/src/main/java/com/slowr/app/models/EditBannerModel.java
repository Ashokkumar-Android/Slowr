package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class EditBannerModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    EditBannerDataModel editBannerDataModel;

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

    public EditBannerDataModel getEditBannerDataModel() {
        return editBannerDataModel;
    }

    public void setEditBannerDataModel(EditBannerDataModel editBannerDataModel) {
        this.editBannerDataModel = editBannerDataModel;
    }
}
