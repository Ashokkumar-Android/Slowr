package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class EditAdModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private EditDataModel editDataModel;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public EditDataModel getEditDataModel() {
        return editDataModel;
    }

    public void setEditDataModel(EditDataModel editDataModel) {
        this.editDataModel = editDataModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


