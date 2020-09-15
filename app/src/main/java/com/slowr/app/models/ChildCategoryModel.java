package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChildCategoryModel {
    @SerializedName("status")
    private boolean status;

    @SerializedName("childGroup")
    private ArrayList<SubCategoryChildModel> childCategoryList;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ArrayList<SubCategoryChildModel> getChildCategoryList() {
        return childCategoryList;
    }

    public void setChildCategoryList(ArrayList<SubCategoryChildModel> childCategoryList) {
        this.childCategoryList = childCategoryList;
    }
}
