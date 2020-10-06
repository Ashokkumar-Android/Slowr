package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ChatClearModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ChatClearItem chatClearItem;

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

    public ChatClearItem getChatClearItem() {
        return chatClearItem;
    }

    public void setChatClearItem(ChatClearItem chatClearItem) {
        this.chatClearItem = chatClearItem;
    }
}

