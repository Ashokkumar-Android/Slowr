package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PopularAdDataModel {
//    @SerializedName("path")
//    public String urlPath;
    @SerializedName("ads")
    public ArrayList<AdItemModel> adList;

//    public String getUrlPath() {
//        return urlPath;
//    }
//
//    public void setUrlPath(String urlPath) {
//        this.urlPath = urlPath;
//    }

    public ArrayList<AdItemModel> getAdList() {
        return adList;
    }

    public void setAdList(ArrayList<AdItemModel> adList) {
        this.adList = adList;
    }
}
