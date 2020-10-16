package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class UnreadCountModel {

    @SerializedName("chat_count")
    String chatCount;

    @SerializedName("notification_count")
    String notificationCount;

    public String getChatCount() {
        return chatCount;
    }

    public void setChatCount(String chatCount) {
        this.chatCount = chatCount;
    }

    public String getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }
}
