package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AdItemModel {

    @SerializedName("id")
    private String adId = "";

    @SerializedName("category_id")
    private String catId = "";

    @SerializedName("title")
    private String adTitle = "";

    @SerializedName("description")
    private String adDescription = "";

    @SerializedName("rental_duration")
    private String adDuration = "";

    @SerializedName("rental_fee")
    private String adFee = "";

    @SerializedName("is_rent_negotiable")
    private String adNegotiable = "";

    @SerializedName("area")
    private String areaName = "";

    @SerializedName("city")
    private String cityName = "";

    @SerializedName("state_name")
    private String stateName = "";

    @SerializedName("photo")
    private String photoType = "";

    @SerializedName("like_count")
    private String likeCount = "";

    @SerializedName("is_user_fav")
    private String isFavorite = "";

    @SerializedName("is_user_like")
    private String isLike = "";

    @SerializedName("user_id")
    private String UserId = "";

    @SerializedName("type")
    private String adType = "";

    @SerializedName("status")
    private String adStatus;

    @SerializedName("promotion")
    private String adPromotion;

    @SerializedName("cat_group")
    private String catGroup;

    @SerializedName("service_ad_count")
    private int serviceAdCount = 0;

    @SerializedName("prosper_id")
    private String prosperId;

    @SerializedName("parent_id")
    private String adParentId;

    @SerializedName("user_viewed_count")
    private String userViewCount;

    @SerializedName("prosper_page_view_count")
    private String prosperPageViewCount;

    boolean isProgress = false;

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

    public String getAdDescription() {
        return adDescription;
    }

    public void setAdDescription(String adDescription) {
        this.adDescription = adDescription;
    }

    public String getAdDuration() {
        return adDuration;
    }

    public void setAdDuration(String adDuration) {
        this.adDuration = adDuration;
    }

    public String getAdFee() {
        return adFee;
    }

    public void setAdFee(String adFee) {
        this.adFee = adFee;
    }

    public String getAdNegotiable() {
        return adNegotiable;
    }

    public void setAdNegotiable(String adNegotiable) {
        this.adNegotiable = adNegotiable;
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

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public boolean isProgress() {
        return isProgress;
    }

    public void setProgress(boolean progress) {
        isProgress = progress;
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

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getAdPromotion() {
        return adPromotion;
    }

    public void setAdPromotion(String adPromotion) {
        this.adPromotion = adPromotion;
    }

    public String getCatGroup() {
        return catGroup;
    }

    public void setCatGroup(String catGroup) {
        this.catGroup = catGroup;
    }

    public int getServiceAdCount() {
        return serviceAdCount;
    }

    public void setServiceAdCount(int serviceAdCount) {
        this.serviceAdCount = serviceAdCount;
    }

    public String getProsperId() {
        return prosperId;
    }

    public void setProsperId(String prosperId) {
        this.prosperId = prosperId;
    }

    public String getAdParentId() {
        return adParentId;
    }

    public void setAdParentId(String adParentId) {
        this.adParentId = adParentId;
    }

    public String getUserViewCount() {
        return userViewCount;
    }

    public void setUserViewCount(String userViewCount) {
        this.userViewCount = userViewCount;
    }

    public String getProsperPageViewCount() {
        return prosperPageViewCount;
    }

    public void setProsperPageViewCount(String prosperPageViewCount) {
        this.prosperPageViewCount = prosperPageViewCount;
    }
}
