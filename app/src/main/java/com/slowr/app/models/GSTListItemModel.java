package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class GSTListItemModel {
    @SerializedName("id")
    String gstId;
    @SerializedName("name")
    String companyName;
    @SerializedName("gst_no")
    String gstNo;
    @SerializedName("address")
    String companyAddress;

    public String getGstId() {
        return gstId;
    }

    public void setGstId(String gstId) {
        this.gstId = gstId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getGstNo() {
        return gstNo;
    }

    public void setGstNo(String gstNo) {
        this.gstNo = gstNo;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
}
