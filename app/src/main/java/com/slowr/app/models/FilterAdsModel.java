package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FilterAdsModel {
    @SerializedName("current_page")
    int currentPage;
    @SerializedName("last_page")
    int lastPage;
    @SerializedName("total")
    int totalAdsCount;
    @SerializedName("data")
    public ArrayList<AdItemModel> adList;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public int getTotalAdsCount() {
        return totalAdsCount;
    }

    public void setTotalAdsCount(int totalAdsCount) {
        this.totalAdsCount = totalAdsCount;
    }

    public ArrayList<AdItemModel> getAdList() {
        return adList;
    }

    public void setAdList(ArrayList<AdItemModel> adList) {
        this.adList = adList;
    }
}
