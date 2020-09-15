package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class InvoiceItemModel {
    @SerializedName("id")
    private String invoiceId;

    @SerializedName("invoice_no")
    private String invoiceNo;

    @SerializedName("total_amount")
    private String totalAmount;

    @SerializedName("description")
    private String invoiceTitle;

    @SerializedName("date")
    private String invoiceDate;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
}
