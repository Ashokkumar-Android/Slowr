package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ColorCodeItemModel {
    @SerializedName("from")
    String colorOne;
    @SerializedName("to")
    String colorTwo;
    boolean isChange = false;

    public String getColorOne() {
        return colorOne;
    }

    public void setColorOne(String colorOne) {
        this.colorOne = colorOne;
    }

    public String getColorTwo() {
        return colorTwo;
    }

    public void setColorTwo(String colorTwo) {
        this.colorTwo = colorTwo;
    }

    public boolean isChange() {
        return isChange;
    }

    public void setChange(boolean change) {
        isChange = change;
    }

    public ColorCodeItemModel(String colorOne, String colorTwo, boolean isChange) {
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
        this.isChange = isChange;
    }
}
