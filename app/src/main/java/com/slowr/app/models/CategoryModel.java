package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class CategoryModel {

    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    private CategoryServiceModel categoryServiceModel;



    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public CategoryServiceModel getCategoryServiceModel() {
        return categoryServiceModel;
    }

    public void setCategoryServiceModel(CategoryServiceModel categoryServiceModel) {
        this.categoryServiceModel = categoryServiceModel;
    }
}
