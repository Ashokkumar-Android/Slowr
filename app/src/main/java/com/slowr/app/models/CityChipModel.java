package com.slowr.app.models;


import com.slowr.materialchips.model.ChipInterface;

public class CityChipModel implements ChipInterface {

    private String id;
    private String cityName;
    private String cityPrice;

    public CityChipModel(String id, String name, String phoneNumber) {
        this.id = id;
        this.cityName = name;
        this.cityPrice = phoneNumber;
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public String getCityName() {
        return cityName;
    }

    @Override
    public String getPrice() {
        return cityPrice;
    }
}
