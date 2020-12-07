package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class AppVersionDataModel {

    @SerializedName("is_forced")
    boolean isForced;


    @SerializedName("is_update")
    boolean isUpdate;

    @SerializedName("message")
    String updateMessage;

    public boolean isForced() {
        return isForced;
    }

    public void setForced(boolean forced) {
        isForced = forced;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }
}
