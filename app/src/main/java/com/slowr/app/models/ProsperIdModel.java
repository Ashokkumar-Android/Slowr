package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProsperIdModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<ProsperIdItemModel> prosperIdItemModel;

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

    public ArrayList<ProsperIdItemModel> getProsperIdItemModel() {
        return prosperIdItemModel;
    }

    public void setProsperIdItemModel(ArrayList<ProsperIdItemModel> prosperIdItemModel) {
        this.prosperIdItemModel = prosperIdItemModel;
    }
}
