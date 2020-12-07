package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class PrivacyModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private String privacyData;

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

    public String getPrivacyData() {
        return privacyData;
    }

    public void setPrivacyData(String privacyData) {
        this.privacyData = privacyData;
    }
}
