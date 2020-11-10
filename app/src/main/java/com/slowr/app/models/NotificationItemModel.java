package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class NotificationItemModel {

    @SerializedName("id")
    String notificationId;

    @SerializedName("type")
    String notificationType;

    @SerializedName("ads_id")
    String adId;

    @SerializedName("user_id")
    String userId;

    @SerializedName("category_id")
    String catId;

    @SerializedName("message")
    String notificationContent;

    @SerializedName("color")
    String notificationColor;

    @SerializedName("created_at")
    Date notificationDate;

    @SerializedName("is_read")
    String isRead;

    boolean isCheck = false;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getNotificationColor() {
        return notificationColor;
    }

    public void setNotificationColor(String notificationColor) {
        this.notificationColor = notificationColor;
    }

    public Date getNotificationDate() {
        return notificationDate;
    }

    public void setNotificationDate(Date notificationDate) {
        this.notificationDate = notificationDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
