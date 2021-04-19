package com.slowr.app.models.FilterResult;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("prosper_id")
    String prosperId;
    @SerializedName("id")
    String userId;

    public String getProsperId() {
        return prosperId;
    }

    public void setProsperId(String prosperId) {
        this.prosperId = prosperId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
