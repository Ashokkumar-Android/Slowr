package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChatHistoryModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<ChatItemModel> chatList;

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

    public ArrayList<ChatItemModel> getChatList() {
        return chatList;
    }

    public void setChatList(ArrayList<ChatItemModel> chatList) {
        this.chatList = chatList;
    }
}
