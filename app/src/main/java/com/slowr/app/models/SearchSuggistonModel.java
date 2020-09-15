package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchSuggistonModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private ArrayList<SuggistionItem> searchSugList;

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

    public ArrayList<SuggistionItem> getSearchSugList() {
        return searchSugList;
    }

    public void setSearchSugList(ArrayList<SuggistionItem> searchSugList) {
        this.searchSugList = searchSugList;
    }
}
