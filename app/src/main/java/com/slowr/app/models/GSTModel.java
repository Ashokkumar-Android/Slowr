package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class GSTModel {
    @SerializedName("error")
    boolean isError;

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }
}
