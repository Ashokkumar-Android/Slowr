package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ColorItemModel {

    @SerializedName("colourData")
    ArrayList<String> colorCode;

    @SerializedName("guideLines")
    String guideLines;

    @SerializedName("cities")
    private ArrayList<CityItemModel> cityList;

    public ArrayList<String> getColorCode() {
        return colorCode;
    }

    public void setColorCode(ArrayList<String> colorCode) {
        this.colorCode = colorCode;
    }

    public String getGuideLines() {
        return guideLines;
    }

    public void setGuideLines(String guideLines) {
        this.guideLines = guideLines;
    }

    public ArrayList<CityItemModel> getCityList() {
        return cityList;
    }

    public void setCityList(ArrayList<CityItemModel> cityList) {
        this.cityList = cityList;
    }
}
