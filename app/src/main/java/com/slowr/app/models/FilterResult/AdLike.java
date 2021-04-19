package com.slowr.app.models.FilterResult;

import com.google.gson.annotations.SerializedName;

public class AdLike {
    @SerializedName("ads_id")
    String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
