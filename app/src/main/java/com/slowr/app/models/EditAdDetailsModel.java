package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class EditAdDetailsModel {

    @SerializedName("id")
    private String adId;

    @SerializedName("category_id")
    private String catId;


    @SerializedName("title")
    private String adTitle;

    @SerializedName("status")
    private String adStatus;


    @SerializedName("description")
    private String description;

    @SerializedName("rental_duration")
    private String rentalDuration;

    @SerializedName("rental_fee")
    private String rentalFee;

    @SerializedName("is_rent_negotiable")
    private String isNegotiable;

    @SerializedName("city_id")
    private String cityId;

    @SerializedName("locality_id")
    private String areaId;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("is_mobile_visible")
    private String isMobileVisible;
    @SerializedName("like_count")
    private String likeCount;

    @SerializedName("fav_count")
    private String favCount;

    @SerializedName("is_user_fav")
    private String isFavorite;

    @SerializedName("is_user_like")
    private String isLike;

    @SerializedName("area")
    private String areaName;

    @SerializedName("city")
    private String cityName;

    @SerializedName("state_name")
    private String stateName;

    @SerializedName("type")
    private String adType = "";

    @SerializedName("promotion")
    private String adPromotion;

    @SerializedName("user_id")
    private String userId;
    @SerializedName("parent_id")
    private String parentId;

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

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getIsNegotiable() {
        return isNegotiable;
    }

    public void setIsNegotiable(String isNegotiable) {
        this.isNegotiable = isNegotiable;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIsMobileVisible() {
        return isMobileVisible;
    }

    public void setIsMobileVisible(String isMobileVisible) {
        this.isMobileVisible = isMobileVisible;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getFavCount() {
        return favCount;
    }

    public void setFavCount(String favCount) {
        this.favCount = favCount;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdPromotion() {
        return adPromotion;
    }

    public void setAdPromotion(String adPromotion) {
        this.adPromotion = adPromotion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
