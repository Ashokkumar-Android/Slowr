package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ChatItemModel {
    @SerializedName("id")
    String chatId = "";

    @SerializedName("user_id")
    String userId;

    @SerializedName("message")
    String chatMessage;

    @SerializedName("ads_id")
    String adId;

    @SerializedName("category_id")
    String catId;

    @SerializedName("render_id")
    String renterId;

    @SerializedName("created_at")
    String dateTime;

    @SerializedName("photo")
    String imgUrl;

    @SerializedName("date")
    String chatDate;

    @SerializedName("time")
    String chatTime;

    @SerializedName("last_id")
    String lastId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
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

    public String getRenterId() {
        return renterId;
    }

    public void setRenterId(String renterId) {
        this.renterId = renterId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getChatDate() {
        return chatDate;
    }

    public void setChatDate(String chatDate) {
        this.chatDate = chatDate;
    }

    public String getChatTime() {
        return chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
}
