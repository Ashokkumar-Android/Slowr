package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class CountModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    UnreadCountModel unreadCountModel;

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

    public UnreadCountModel getUnreadCountModel() {
        return unreadCountModel;
    }

    public void setUnreadCountModel(UnreadCountModel unreadCountModel) {
        this.unreadCountModel = unreadCountModel;
    }
}
