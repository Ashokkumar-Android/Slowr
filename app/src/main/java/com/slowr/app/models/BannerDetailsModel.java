package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class BannerDetailsModel {
    @SerializedName("id")
    String bannerId;

    @SerializedName("title")
    String bannerTitle;

    @SerializedName("fromDate")
    String fromDate;

    @SerializedName("toDate")
    String toDate;

    @SerializedName("description")
    String description;

    @SerializedName("city_id")
    String cityIds;

    @SerializedName("amount")
    String totalAmount;

    @SerializedName("total_days")
    String totalDays;

    @SerializedName("status")
    String adStatus;

    @SerializedName("colour_code")
    String colorCode;



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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}


