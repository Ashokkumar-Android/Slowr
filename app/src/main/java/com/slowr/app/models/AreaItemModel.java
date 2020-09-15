package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AreaItemModel {

    @SerializedName("id")
    private String areaId;

    @SerializedName("area")
    private String areaName;

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}
