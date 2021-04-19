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

    public boolean isSearch = false;
    public boolean isSelect = false;

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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public FiltersModel(String filterId, String filterTitle, ArrayList<SortByModel> filterValue, String selectedValue, boolean isSearch, boolean _isSelcet) {
        this.filterId = filterId;
        this.filterTitle = filterTitle;
        this.filterValue = filterValue;
        this.selectedValue = selectedValue;
        this.isSearch = isSearch;
        this.isSelect = _isSelcet;
    }
}
