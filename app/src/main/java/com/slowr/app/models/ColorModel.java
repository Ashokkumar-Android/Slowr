package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ColorModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ColorItemModel colorItemModel;

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

    public ColorItemModel getColorItemModel() {
        return colorItemModel;
    }

    public void setColorItemModel(ColorItemModel colorItemModel) {
        this.colorItemModel = colorItemModel;
    }
}
