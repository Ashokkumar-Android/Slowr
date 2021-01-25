package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FiltersModel {
    @SerializedName("id")
    public String filterId;

    @SerializedName("title")
    public String filterTitle;

    @SerializedName("value")
    public ArrayList<SortByModel> filterValue;

    String selectedValue = "Any";

    @SerializedName("is_searchable")
    public boolean isSearch = false;

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }

    public ArrayList<SortByModel> getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(ArrayList<SortByModel> filterValue) {
        this.filterValue = filterValue;
    }

    public String getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public boolean isSearch() {
        return isSearch;
    }

    public void setSearch(boolean search) {
        isSearch = search;
    }
}
