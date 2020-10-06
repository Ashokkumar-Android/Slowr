package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductChatModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<ProductChatItemModel> productChatList;

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

    public ArrayList<ProductChatItemModel> getProductChatList() {
        return productChatList;
    }

    public void setProductChatList(ArrayList<ProductChatItemModel> productChatList) {
        this.productChatList = productChatList;
    }
}
