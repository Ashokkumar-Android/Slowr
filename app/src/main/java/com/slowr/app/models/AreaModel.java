package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AreaModel {
    @SerializedName("status")
   private boolean status;

    @SerializedName("area")
    private ArrayList<AreaItemModel> areaList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<AreaItemModel> getAreaList() {
        return areaList;
    }

    public void setAreaList(ArrayList<AreaItemModel> areaList) {
        this.areaList = areaList;
    }
}
