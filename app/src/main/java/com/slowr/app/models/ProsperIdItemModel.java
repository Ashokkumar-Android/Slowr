package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ProsperIdItemModel {
    @SerializedName("prosper_id")
    private String prosperId;

    @SerializedName("category")
    private String prosCategory;

    @SerializedName("price")
    private String prosPrice;

    public String getProsperId() {
        return prosperId;
    }

    public void setProsperId(String prosperId) {
        this.prosperId = prosperId;
    }

    public String getProsCategory() {
        return prosCategory;
    }

    public void setProsCategory(String prosCategory) {
        this.prosCategory = prosCategory;
    }

    public String getProsPrice() {
        return prosPrice;
    }

    public void setProsPrice(String prosPrice) {
        this.prosPrice = prosPrice;
    }
}
