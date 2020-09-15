package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class CityItemModel {

    @SerializedName("id")
    private String cityId;

    @SerializedName("city")
    private String cityName;

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
