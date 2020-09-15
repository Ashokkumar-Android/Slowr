package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class PromotePriceModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    PromotePriceItemModel priceItemModel;

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

    public PromotePriceItemModel getPriceItemModel() {
        return priceItemModel;
    }

    public void setPriceItemModel(PromotePriceItemModel priceItemModel) {
        this.priceItemModel = priceItemModel;
    }
}
