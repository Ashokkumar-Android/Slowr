package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ProductChatItemModel {
    @SerializedName("category_id")
    String catId;

    @SerializedName("ads_id")
    String adId;

    @SerializedName("user_id")
    String userId;

    @SerializedName("render_id")
    String renderId;

    @SerializedName("render_ids")
    String renderPassId;

    @SerializedName("title")
    String productTitle;

    @SerializedName("prosper_id")
    String prosperId;

    @SerializedName("rental_duration")
    String rentalDuration;

    @SerializedName("rental_fee")
    String rentalFee;

    @SerializedName("photo")
    String adImage;

    @SerializedName("id")
    String chatAdId;

    @SerializedName("last_id")
    String lastId;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRenderId() {
        return renderId;
    }

    public void setRenderId(String renderId) {
        this.renderId = renderId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProsperId() {
        return prosperId;
    }

    public void setProsperId(String prosperId) {
        this.prosperId = prosperId;
    }

    public String getRentalDuration() {
        return rentalDuration;
    }

    public void setRentalDuration(String rentalDuration) {
        this.rentalDuration = rentalDuration;
    }

    public String getRentalFee() {
        return rentalFee;
    }

    public void setRentalFee(String rentalFee) {
        this.rentalFee = rentalFee;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getChatAdId() {
        return chatAdId;
    }

    public String getRenderPassId() {
        return renderPassId;
    }

    public void setRenderPassId(String renderPassId) {
        this.renderPassId = renderPassId;
    }

    public void setChatAdId(String chatAdId) {
        this.chatAdId = chatAdId;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }
}
