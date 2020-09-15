package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PopularAdModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

//    @SerializedName("path")
//    public String urlPath;

    @SerializedName("sortBy")
    public ArrayList<SortByModel> sortByModel;

    @SerializedName("filters")
    public ArrayList<FiltersModel> filterModel;

//    @SerializedName("data")
//    private PopularAdDataModel adDataModel;

    @SerializedName("data")
    public ArrayList<AdItemModel> adList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SortByModel> getSortByModel() {
        return sortByModel;
    }

    public void setSortByModel(ArrayList<SortByModel> sortByModel) {
        this.sortByModel = sortByModel;
    }

    public ArrayList<FiltersModel> getFilterModel() {
        return filterModel;
    }

    public void setFilterModel(ArrayList<FiltersModel> filterModel) {
        this.filterModel = filterModel;
    }
}
