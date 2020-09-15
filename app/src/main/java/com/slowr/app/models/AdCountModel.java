package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AdCountModel {
    @SerializedName("active_ads")
    String activeAdCount;

    @SerializedName("in_active_ads")
    String inActiveAdCount;

    @SerializedName("total_ads")
    String totalAdCount;

    public String getActiveAdCount() {
        return activeAdCount;
    }

    public void setActiveAdCount(String activeAdCount) {
        this.activeAdCount = activeAdCount;
    }

    public String getInActiveAdCount() {
        return inActiveAdCount;
    }

    public void setInActiveAdCount(String inActiveAdCount) {
        this.inActiveAdCount = inActiveAdCount;
    }

    public String getTotalAdCount() {
        return totalAdCount;
    }

    public void setTotalAdCount(String totalAdCount) {
        this.totalAdCount = totalAdCount;
    }
}
