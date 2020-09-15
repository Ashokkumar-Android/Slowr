package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ReportTypeModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<SortByModel> reportTypeList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<SortByModel> getReportTypeList() {
        return reportTypeList;
    }

    public void setReportTypeList(ArrayList<SortByModel> reportTypeList) {
        this.reportTypeList = reportTypeList;
    }
}
