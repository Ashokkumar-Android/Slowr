package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ReportTicketModel {
    @SerializedName("ticket_no")
    private String ticketNo;

    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }
}
