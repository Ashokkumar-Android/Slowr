package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class RegCityItemModel {
    @SerializedName("id")
    String stateId;

    @SerializedName("name")
    String stateName;

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
