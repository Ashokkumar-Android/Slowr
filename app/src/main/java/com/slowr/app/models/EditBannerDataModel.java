package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EditBannerDataModel {

    @SerializedName("banner")
    BannerDetailsModel bannerDetailsModel;

    @SerializedName("photos")
    String bannerImage;

    @SerializedName("city")
    String cityNames;

    @SerializedName("city_id")
    String cityIds;

    @SerializedName("cities")
    private ArrayList<CityItemModel> cityList;

    @SerializedName("colourData")
    ArrayList<ColorCodeItemModel> colorCode;

    @SerializedName("guideLines")
    String guideLines;

    public BannerDetailsModel getBannerDetailsModel() {
        return bannerDetailsModel;
    }

    public void setBannerDetailsModel(BannerDetailsModel bannerDetailsModel) {
        this.bannerDetailsModel = bannerDetailsModel;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public ArrayList<CityItemModel> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityItemModel> cityList) {
        this.cityList = cityList;
    }

    public ArrayList<ColorCodeItemModel> getColorCode() {
        return colorCode;
    }

    public void setColorCode(ArrayList<ColorCodeItemModel> colorCode) {
        this.colorCode = colorCode;
    }

    public String getGuideLines() {
        return guideLines;
    }

    public void setGuideLines(String guideLines) {
        this.guideLines = guideLines;
    }

    public String getCityNames() {
        return cityNames;
    }

    public void setCityNames(String cityNames) {
        this.cityNames = cityNames;
    }

    public String getCityIds() {
        return cityIds;
    }

    public void setCityIds(String cityIds) {
        this.cityIds = cityIds;
    }
}
