
package com.slowr.app.models.Fillter;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterModel {

    @SerializedName("data")
    @Expose
    private Data data;



    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
