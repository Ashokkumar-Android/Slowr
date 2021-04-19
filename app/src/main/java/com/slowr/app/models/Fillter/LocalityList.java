
package com.slowr.app.models.Fillter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocalityList {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("value")
    @Expose
    private List<LocalityValue> value = null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<LocalityValue> getValue() {
        return value;
    }

    public void setValue(List<LocalityValue> value) {
        this.value = value;
    }

}
