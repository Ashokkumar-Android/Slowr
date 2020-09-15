package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ReportResponsModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ReportTicketModel reportTicketModel;

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

    public ReportTicketModel getReportTicketModel() {
        return reportTicketModel;
    }

    public void setReportTicketModel(ReportTicketModel reportTicketModel) {
        this.reportTicketModel = reportTicketModel;
    }
}
