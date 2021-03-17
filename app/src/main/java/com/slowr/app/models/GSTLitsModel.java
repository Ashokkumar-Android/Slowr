package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GSTLitsModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private ArrayList<GSTListItemModel> gstList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<GSTListItemModel> getGstList() {
        return gstList;
    }

    public void setGstList(ArrayList<GSTListItemModel> gstList) {
        this.gstList = gstList;
    }
}
