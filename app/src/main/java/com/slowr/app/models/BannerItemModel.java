package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class BannerItemModel {
    @SerializedName("id")
    String bannerId;

    @SerializedName("title")
    String bannerTitle;

    @SerializedName("fromDate")
    String bannerFromDate;

    @SerializedName("toDate")
    String bannerToDate;

    @SerializedName("status")
    String bannerStatus;

    @SerializedName("colour_code")
    String colorCode;

    @SerializedName("photo")
    String bannerImage;

    @SerializedName("description")
    String description;

    @SerializedName("prosper_id")
    String prosperId;


    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getBannerTitle() {
        return bannerTitle;
    }

    public void setBannerTitle(String bannerTitle) {
        this.bannerTitle = bannerTitle;
    }

    public String getBannerFromDate() {
        return bannerFromDate;
    }

    public void setBannerFromDate(String bannerFromDate) {
        this.bannerFromDate = bannerFromDate;
    }

    public String getBannerToDate() {
        return bannerToDate;
    }

    public void setBannerToDate(String bannerToDate) {
        this.bannerToDate = bannerToDate;
    }

    public String getBannerStatus() {
        return bannerStatus;
    }

    public void setBannerStatus(String bannerStatus) {
        this.bannerStatus = bannerStatus;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProsperId() {
        return prosperId;
    }

    public void setProsperId(String prosperId) {
        this.prosperId = prosperId;
    }
}
