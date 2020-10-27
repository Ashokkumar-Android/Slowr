package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class CityItemModel {

    @SerializedName("id")
    private String cityId;

    @SerializedName("city")
    private String cityName;

    @SerializedName("price")
    private String cityPrice;

    boolean isSelect = false;

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

    public String getCityPrice() {
        return cityPrice;
    }

    public void setCityPrice(String cityPrice) {
        this.cityPrice = cityPrice;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public CityItemModel(String cityId, String cityName, String cityPrice, boolean isSelect) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.cityPrice = cityPrice;
        this.isSelect = isSelect;
    }
}
