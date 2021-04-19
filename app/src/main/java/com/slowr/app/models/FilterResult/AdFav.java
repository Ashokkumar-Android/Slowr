package com.slowr.app.models.FilterResult;

import com.google.gson.annotations.SerializedName;

public class AdFav {
    @SerializedName("ads_id")
    String adId;

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

}
