package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SubCategoryModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private ArrayList<SubCategoryItemModel> subCategoryList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<SubCategoryItemModel> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(ArrayList<SubCategoryItemModel> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }
}
