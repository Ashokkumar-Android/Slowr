package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<NotificationItemModel> notificationItemModelArrayList;

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

    public ArrayList<NotificationItemModel> getNotificationItemModelArrayList() {
        return notificationItemModelArrayList;
    }

    public void setNotificationItemModelArrayList(ArrayList<NotificationItemModel> notificationItemModelArrayList) {
        this.notificationItemModelArrayList = notificationItemModelArrayList;
    }
}
