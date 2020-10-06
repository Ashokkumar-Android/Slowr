package com.slowr.app.models;

import com.google.gson.annotations.SerializedName;

public class ChatClearItem {
    @SerializedName("last_id")
    String lastName;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
