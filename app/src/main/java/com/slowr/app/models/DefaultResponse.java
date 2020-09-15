package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("field")
    private String fileld;

    @SerializedName("action")
    private String action;

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

    public String getFileld() {
        return fileld;
    }

    public void setFileld(String fileld) {
        this.fileld = fileld;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
