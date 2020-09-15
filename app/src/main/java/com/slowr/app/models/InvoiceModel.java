package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InvoiceModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    ArrayList<InvoiceItemModel> invoiceItemModels;

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

    public ArrayList<InvoiceItemModel> getInvoiceItemModels() {
        return invoiceItemModels;
    }

    public void setInvoiceItemModels(ArrayList<InvoiceItemModel> invoiceItemModels) {
        this.invoiceItemModels = invoiceItemModels;
    }
}
